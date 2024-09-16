package com.sanisamoj.routing

import com.sanisamoj.data.models.dataclass.CreateEventRequest
import com.sanisamoj.data.models.dataclass.EventLoggerFilter
import com.sanisamoj.data.models.dataclass.LogEventResponse
import com.sanisamoj.data.models.dataclass.LogEventWithPaginationResponse
import com.sanisamoj.services.logEvent.EventService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.logEventRouting() {
    route("/log") {

        rateLimit(RateLimitName("lightweight")) {

            authenticate("application-jwt") {

                // Route responsible for recording a log
                post {
                    val createEventRequest: CreateEventRequest = call.receive<CreateEventRequest>()
                    val principal: JWTPrincipal? = call.principal<JWTPrincipal>()
                    val id: String = principal?.payload?.getClaim("id")?.asString()!!
                    val eventRequestUpdated = createEventRequest.copy(id = id)

                    val eventResponse: LogEventResponse = EventService().registerEvent(eventRequestUpdated)
                    return@post call.respond(HttpStatusCode.Created, eventResponse)
                }
            }

            authenticate("operator-jwt") {

                // Responsible for returning to delete a logEvent
                delete {
                    val eventId: String = call.request.queryParameters["id"].toString()
                    EventService().deleteEvent(eventId)
                    return@delete call.respond(HttpStatusCode.OK)
                }

                // Responsible for marking a log as read or unread
                put("/{id}") {
                    val eventId: String = call.parameters["id"].toString()
                    val parameter: String = call.request.queryParameters["read"].toString().lowercase()
                    val read: Boolean = parameter == "true"

                    EventService().readEvent(eventId, read)
                    return@put call.respond(HttpStatusCode.OK)
                }
            }

            // Responsible for returning multiple logs with filter
            get{
                val eventType: String? = call.request.queryParameters["type"]
                val eventSeverity: String? = call.request.queryParameters["severity"]
                val mode: String? = call.request.queryParameters["mode"]
                val read: String? = call.request.queryParameters["read"]
                val pageParameter: String? = call.request.queryParameters["page"]
                val sizeParameter: String? = call.request.queryParameters["size"]

                val actualRead = if(read == null) {
                    null
                } else {
                    if(read.lowercase() == "true") true else false
                }
                val page = pageParameter?.toInt() ?: 1
                val size = sizeParameter?.toInt() ?: 20

                if(eventType == null || eventSeverity == null || mode == null) {
                    val logEventList: LogEventWithPaginationResponse = EventService().getAllEvents(actualRead, page, size)
                    return@get call.respond(logEventList)
                }

                val eventLoggerFilter = EventLoggerFilter(
                    eventType = eventType.toString().uppercase(),
                    eventSeverity = eventSeverity.toString().uppercase(),
                    ordering = if(mode.lowercase() == "asc") 1 else -1,
                    read = if(read.toString().lowercase() == "true") true else false,
                    page = page,
                    size = size
                )

                val logEventList = EventService().getAllEventsByFilter(eventLoggerFilter)
                return@get call.respond(logEventList)
            }
        }
    }
}
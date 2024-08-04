package com.sanisamoj.routing

import com.sanisamoj.data.models.dataclass.CreateEventRequest
import com.sanisamoj.data.models.dataclass.EventLoggerFilter
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
                    val createEventRequest = call.receive<CreateEventRequest>()
                    val principal = call.principal<JWTPrincipal>()
                    val id = principal?.payload?.getClaim("id")?.asString()!!
                    val eventRequestUpdated = createEventRequest.copy(id = id)

                    val eventResponse = EventService().registerEvent(eventRequestUpdated)
                    return@post call.respond(HttpStatusCode.Created, eventResponse)
                }
            }

            authenticate("operator-jwt") {

                // Responsible for returning to delete a logEvent
                delete {
                    val eventId = call.request.queryParameters["id"].toString()
                    EventService().deleteEvent(eventId)
                    return@delete call.respond(HttpStatusCode.OK)
                }

                // Responsible for marking a log as read or unread
                put("/{id}") {
                    val eventId = call.parameters["id"].toString()
                    val parameter = call.request.queryParameters["read"].toString().toLowerCase()
                    val read = parameter == "true"

                    EventService().readEvent(eventId, read)
                    return@put call.respond(HttpStatusCode.OK)
                }
            }

            // Responsible for returning multiple logs with filter
            get{
                val eventType = call.request.queryParameters["type"]
                val eventSeverity = call.request.queryParameters["severity"]
                val mode = call.request.queryParameters["mode"]
                val read = call.request.queryParameters["read"]
                val pageParameter = call.request.queryParameters["page"]
                val sizeParameter = call.request.queryParameters["size"]

                val actualRead = if(read == null) {
                    null
                } else {
                    if(read.lowercase() == "true") true else false
                }
                val page = pageParameter?.toInt() ?: 1
                val size = sizeParameter?.toInt() ?: 20

                if(eventType == null || eventSeverity == null || mode == null) {
                    val logEventList = EventService().getAllEvents(actualRead, page, size)
                    return@get call.respond(logEventList)
                }

                val eventLoggerFilter = EventLoggerFilter(
                    eventType = eventType.toString().toUpperCase(),
                    eventSeverity = eventSeverity.toString().toUpperCase(),
                    ordering = if(mode.toLowerCase() == "asc") 1 else -1,
                    read = if(read.toString().toLowerCase() == "true") true else false,
                    page = page,
                    size = size
                )

                val logEventList = EventService().getAllEventsByFilter(eventLoggerFilter)
                return@get call.respond(logEventList)
            }
        }
    }
}
package com.sanisamoj.controllers

import com.sanisamoj.data.models.generics.EventLoggerFilter
import com.sanisamoj.data.models.requests.CreateEventRequest
import com.sanisamoj.errors.errorResponse
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

                // Rota responsável por registrar um log
                post {
                    val createEventRequest = call.receive<CreateEventRequest>()
                    val principal = call.principal<JWTPrincipal>()
                    val id = principal?.payload?.getClaim("id")?.asString()!!
                    val eventRequestUpdated = createEventRequest.copy(id = id)

                    try {
                        val eventResponse = EventService().registerEvent(eventRequestUpdated)
                        return@post call.respond(HttpStatusCode.Created, eventResponse)

                    } catch (e: Throwable) {
                        val response = errorResponse(e.message!!)
                        return@post call.respond(response.first, message = response.second)
                    }
                }
            }

            authenticate("operator-jwt") {

                // Responsável por retornar deletar um logEvent
                delete {
                    val eventId = call.request.queryParameters["id"].toString()
                    EventService().deleteEvent(eventId)
                    return@delete call.respond(HttpStatusCode.OK)
                }

                // Responsável por marcar como lido, ou não lido um log
                put("/{id}") {
                    val eventId = call.parameters["id"].toString()
                    val parameter = call.request.queryParameters["read"].toString().toLowerCase()
                    val read = if(parameter == "true") true else false
                    EventService().readEvent(eventId, read)
                    return@put call.respond(HttpStatusCode.OK)
                }
            }

            // Responsável por retornar vários logs com filtro
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

                try {
                    val logEventList = EventService().getAllEventsByFilter(eventLoggerFilter)
                    return@get call.respond(logEventList)

                } catch (e: Throwable) {
                    val response = errorResponse(e.message!!)
                    return@get call.respond(response.first, message = response.second)
                }
            }
        }
    }
}
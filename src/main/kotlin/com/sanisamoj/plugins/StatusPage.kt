package com.sanisamoj.plugins

import com.sanisamoj.context.GlobalContext.errorMessages
import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.data.models.responses.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.statusPage() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val response = HttpStatusCode.InternalServerError to ErrorResponse(errorMessages.unableToComplete, "$cause")
            call.respond(response.first, response.second)
        }

        status(HttpStatusCode.TooManyRequests) { call, status ->
            val retryAfter = call.response.headers["Retry-After"]
            val errorResponse = ErrorResponse(Errors.TooManyRequests.description, "Wait for $retryAfter seconds.")
            call.respond(status, errorResponse)
        }
    }
}
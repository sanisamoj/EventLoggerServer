package com.sanisamoj.plugins

import com.sanisamoj.routing.applicationServiceRouting
import com.sanisamoj.routing.logEventRouting
import com.sanisamoj.routing.operatorRouting
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        operatorRouting()
        applicationServiceRouting()
        logEventRouting()
    }
}

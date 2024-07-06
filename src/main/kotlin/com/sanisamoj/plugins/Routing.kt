package com.sanisamoj.plugins

import com.sanisamoj.controllers.applicationServiceRouting
import com.sanisamoj.controllers.logEventRouting
import com.sanisamoj.controllers.operatorRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        operatorRouting()
        applicationServiceRouting()
        logEventRouting()
    }
}

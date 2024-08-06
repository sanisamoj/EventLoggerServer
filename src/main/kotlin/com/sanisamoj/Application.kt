package com.sanisamoj

import com.sanisamoj.config.Config
import com.sanisamoj.config.GlobalContext
import com.sanisamoj.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureRateLimit()
    configureHTTP()
    statusPage()
    configureSerialization()
    configureSockets()
    configureRouting()
    startBackgroundTasks()
}

fun startBackgroundTasks() {
    CoroutineScope(Dispatchers.Default).launch {
        Config.initialize()
        println(GlobalContext.warningMessagesToChat)
    }
}

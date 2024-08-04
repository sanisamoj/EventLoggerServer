package com.sanisamoj.config

import com.sanisamoj.data.models.dataclass.Connection
import com.sanisamoj.data.models.dataclass.LogEventResponse
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.collections.LinkedHashSet

object WebSocketManager {
    private val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

    fun addConnection(connection: Connection) {
        connections += connection
    }

    fun removeConnection(connection: Connection) {
        connections -= connection
    }

    suspend fun notifyAboutLog(logEventResponse: LogEventResponse) {
        val logEventResponseInString: String = Json.encodeToString(logEventResponse)
        connections.forEach {
            it.session.send(Frame.Text(logEventResponseInString))
        }
    }
}
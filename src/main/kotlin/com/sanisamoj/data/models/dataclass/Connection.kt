package com.sanisamoj.data.models.dataclass

import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Represents a WebSocket connection with a unique ID, associated user ID, and moderator status.
 *
 * @property session the WebSocket session.
 * @property userId the ID of the user associated with this connection.
 */
class Connection(val session: DefaultWebSocketSession, val userId: String) {
    companion object {
        private val lastId = AtomicInteger(0)
    }

    val id: Int = lastId.getAndIncrement()
}
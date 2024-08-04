package com.sanisamoj.data.models.interfaces

import com.sanisamoj.data.models.dataclass.LiveSessions

interface SessionRepository {
    suspend fun setSession(liveSessions: LiveSessions)
    suspend fun getSession(moderatorId: String): LiveSessions?
    suspend fun revokeSession(sessionId: String)
    suspend fun getRevokedSession(sessionId: String): String?
}
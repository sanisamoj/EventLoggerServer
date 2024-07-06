package com.sanisamoj.data.repository

import com.sanisamoj.data.models.generics.LiveSessions
import com.sanisamoj.data.models.interfaces.SessionRepository
import com.sanisamoj.database.redis.CollectionsInRedis
import com.sanisamoj.database.redis.DataIdentificationRedis
import com.sanisamoj.database.redis.Redis

class SessionDefaultRepository : SessionRepository {
    override suspend fun setSession(liveSessions: LiveSessions) {
        val dataIdentificationRedis = DataIdentificationRedis(CollectionsInRedis.LiveTokens, liveSessions.id)
        try {
            Redis.setObject(dataIdentificationRedis, liveSessions)
        } catch (e: Throwable) {}
    }

    override suspend fun getSession(moderatorId: String): LiveSessions? {
        val dataIdentificationRedis = DataIdentificationRedis(CollectionsInRedis.LiveTokens, moderatorId)
        return try {
            Redis.getObject<LiveSessions>(dataIdentificationRedis)
        } catch (e: Throwable) { null }
    }

    override suspend fun revokeSession(sessionId: String) {
        val identification = DataIdentificationRedis(CollectionsInRedis.RevokedTokens, sessionId)
        try {
            Redis.set(identification, sessionId)
        } catch (e:Throwable) {}
    }

    override suspend fun getRevokedSession(sessionId: String): String? {
        val identification = DataIdentificationRedis(CollectionsInRedis.RevokedTokens, sessionId)
        return try {
            Redis.get(identification)
        } catch (e: Throwable) { null }
    }
}
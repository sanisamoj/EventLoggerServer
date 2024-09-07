package com.sanisamoj.config

import com.sanisamoj.database.mongodb.MongoDatabase
import com.sanisamoj.database.redis.Redis

object Config {

    suspend fun initialize() {
        databaseInitialize()
        botApiInitialize()
    }

    private suspend fun databaseInitialize() {
        MongoDatabase.initialize()
        Redis.initialize()
    }

    private suspend fun botApiInitialize() {
        GlobalContext.botRepository.updateToken()
    }
}
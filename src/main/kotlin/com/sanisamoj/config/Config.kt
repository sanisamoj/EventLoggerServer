package com.sanisamoj.config

import com.sanisamoj.data.models.interfaces.BotRepository
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
        val botRepository: BotRepository = GlobalContext.botRepository
        botRepository.updateToken()
    }
}
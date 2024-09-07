package com.sanisamoj.config

import com.sanisamoj.database.mongodb.MongoDatabase
import com.sanisamoj.database.redis.Redis

object Config {

    suspend fun initialize() {
        databaseInitialize()
    }

    private suspend fun databaseInitialize() {
        MongoDatabase.initialize()
        Redis.initialize()
    }
}
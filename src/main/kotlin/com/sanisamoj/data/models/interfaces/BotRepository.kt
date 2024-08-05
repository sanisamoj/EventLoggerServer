package com.sanisamoj.data.models.interfaces

import com.sanisamoj.data.models.dataclass.Bot

interface BotRepository {
    suspend fun createBot(): Bot
    suspend fun sendMessage(message: String, phone: String)
    suspend fun getBot(): Bot
}
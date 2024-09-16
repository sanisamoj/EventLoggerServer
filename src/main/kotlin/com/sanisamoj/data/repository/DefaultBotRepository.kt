package com.sanisamoj.data.repository

import com.sanisamoj.api.BotApiService
import com.sanisamoj.data.models.dataclass.*
import com.sanisamoj.data.models.interfaces.BotRepository
import com.sanisamoj.utils.analyzers.dotEnv
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class DefaultBotRepository(
    private val botApiService: BotApiService
) : BotRepository {
    private val email: String by lazy { dotEnv("BOT_LOGIN_EMAIL") }
    private val password: String by lazy { dotEnv("BOT_LOGIN_PASSWORD") }
    private lateinit var token: String
    private val botId: String by lazy { dotEnv("BOT_ID") }

    override suspend fun updateToken() {
        try {
            val loginRequest = LoginRequest(email, password)
            token = botApiService.login(loginRequest).token
            println("Bot token updated!")
        } catch (_: Throwable) {
            delay(TimeUnit.SECONDS.toMillis(60))
            updateToken()
        }
    }

    override suspend fun sendMessage(messageToSend: MessageToSend) {
        try {
            botApiService.sendMessage(botId, messageToSend, "Bearer $token")
        } catch (_: Throwable) {}
    }
}
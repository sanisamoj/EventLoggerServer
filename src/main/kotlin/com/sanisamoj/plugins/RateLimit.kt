package com.sanisamoj.plugins

import com.sanisamoj.data.models.dataclass.OperatorLoginRequest
import com.sanisamoj.security.AccountAccessGuard
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.request.receiveText
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimit() {
    install(RateLimit) {
        register(RateLimitName("protected")) {
            rateLimiter(limit = 5, refillPeriod = 1.hours)
        }

        register(RateLimitName("lightweight")) {
            rateLimiter(limit = 5, refillPeriod = 1.seconds)
        }

        register(RateLimitName("register")) {
            rateLimiter(limit = 3, refillPeriod = 1.hours)
        }

        register(RateLimitName("login")) {
            rateLimiter(limit = 5, refillPeriod = 1.hours)

            requestKey { applicationCall ->
                val requestBody = applicationCall.receiveText()
                val accountLoginRequest = Json.decodeFromString<OperatorLoginRequest>(requestBody)
                val email = accountLoginRequest.email
                try {
                    AccountAccessGuard().violated(email)
                } catch (_: Throwable) {}
            }
        }
    }
}
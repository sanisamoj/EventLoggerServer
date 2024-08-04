package com.sanisamoj.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.sanisamoj.config.GlobalContext
import com.sanisamoj.data.models.dataclass.OperatorLoginRequest
import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.security.AccountAccessGuard
import com.sanisamoj.utils.analyzers.dotEnv
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

fun Application.configureSecurity() {
    val audience: String = dotEnv("JWT_AUDIENCE")
    val domain: String = dotEnv("JWT_DOMAIN")
    val moderatorSecret = dotEnv("OPERATOR_SECRET")
    val applicationServiceSecret = dotEnv("APPLICATION_SERVICE_SECRET")

    authentication {
        jwt("operator-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(moderatorSecret))
                    .withAudience(audience)
                    .withIssuer(domain)
                    .build()
            )
            validate { credential ->
                val session = credential.payload.getClaim("session").asString()
                val revoked = GlobalContext.sessionRepository.getRevokedSession(session)
                if (revoked != null) throw Exception(Errors.RevokedToken.description)
                if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
            }
        }

        jwt("application-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(applicationServiceSecret))
                    .withAudience(audience)
                    .withIssuer(domain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

fun Application.configureRateLimit() {
    install(RateLimit) {
        register(RateLimitName("protected")) {
            rateLimiter(limit = 5, refillPeriod = 1.hours)
        }

        register(RateLimitName("lightweight")) {
            rateLimiter(limit = 5, refillPeriod = 1.seconds)
        }

        register(RateLimitName("register")) {
            rateLimiter(limit = 2, refillPeriod = 1.hours)
        }

        register(RateLimitName("login")) {
            rateLimiter(limit = 5, refillPeriod = 1.hours)

            requestKey { applicationCall ->
                val requestBody = applicationCall.receiveText()
                val accountLoginRequest = Json.decodeFromString<OperatorLoginRequest>(requestBody)
                val email = accountLoginRequest.email
                try {
                    AccountAccessGuard().violated(email)
                } catch (e: Throwable) {}
            }
        }
    }
}

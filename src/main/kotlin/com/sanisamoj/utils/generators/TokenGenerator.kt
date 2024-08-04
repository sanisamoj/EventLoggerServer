package com.sanisamoj.utils.generators

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.sanisamoj.data.models.dataclass.TokenInfo
import com.sanisamoj.utils.analyzers.dotEnv
import java.util.*

object TokenGenerator {
    private val audience: String = dotEnv("JWT_AUDIENCE")
    private val domain: String = dotEnv("JWT_DOMAIN")
    private val operatorSecret = dotEnv("OPERATOR_SECRET")
    private val applicationServiceSecret = dotEnv("APPLICATION_SERVICE_SECRET")

    fun moderator(tokenInfo: TokenInfo): String {
        val currentTime: Long = System.currentTimeMillis()

        val token: String = JWT.create()
            .withClaim("email", tokenInfo.email)
            .withClaim("id", tokenInfo.id)
            .withClaim("session", tokenInfo.sessionId)
            .withAudience(audience)
            .withIssuer(domain)
            .withExpiresAt(Date(currentTime + tokenInfo.time))
            .sign(Algorithm.HMAC256(operatorSecret))

        return token
    }

    fun applicationService(tokenInfo: TokenInfo): String {
        val currentTime: Long = System.currentTimeMillis()

        val token: String = JWT.create()
            .withClaim("email", tokenInfo.email)
            .withClaim("id", tokenInfo.id)
            .withAudience(audience)
            .withIssuer(domain)
            .withExpiresAt(Date(currentTime + tokenInfo.time))
            .sign(Algorithm.HMAC256(applicationServiceSecret))

        return token
    }
}
package com.sanisamoj.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.sanisamoj.config.GlobalContext
import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.utils.analyzers.dotEnv
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

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



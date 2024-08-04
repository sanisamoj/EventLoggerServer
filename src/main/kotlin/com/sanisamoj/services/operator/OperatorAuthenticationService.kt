package com.sanisamoj.services.operator

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.sanisamoj.context.GlobalContext
import com.sanisamoj.context.GlobalContext.OPERATOR_TOKEN_EXPIRATION
import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.data.models.dataclass.LiveSession
import com.sanisamoj.data.models.dataclass.LiveSessions
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.dataclass.TokenInfo
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.interfaces.SessionRepository
import com.sanisamoj.data.models.dataclass.OperatorLoginRequest
import com.sanisamoj.data.models.dataclass.OperatorLoginResponse
import com.sanisamoj.data.models.dataclass.OperatorResponse
import com.sanisamoj.database.mongodb.Fields
import com.sanisamoj.database.mongodb.OperationField
import com.sanisamoj.services.email.MailNotificationService
import com.sanisamoj.utils.analyzers.dotEnv
import com.sanisamoj.utils.generators.TokenGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class OperatorAuthenticationService(
    private val databaseRepository: DatabaseRepository = GlobalContext.databaseRepository,
    private val sessionRepository: SessionRepository = GlobalContext.sessionRepository
) {
    suspend fun login(login: OperatorLoginRequest): OperatorLoginResponse {
        val operator = databaseRepository.getOperatorByEmail(login.email)
        verifyOperatorStatus(operator)

        val isPasswordCorrect = BCrypt.checkpw(login.password, operator.password)
        if (!isPasswordCorrect) throw Exception(Errors.InvalidEmailOrPassword.description)

        val sessionId = ObjectId().toString()
        val tokenInfo = TokenInfo(
            id = operator.id.toString(),
            email = operator.email,
            sessionId = sessionId,
            time = OPERATOR_TOKEN_EXPIRATION
        )
        val token = TokenGenerator.moderator(tokenInfo)

        registerSession(operator.id.toString(), sessionId)
        val operatorResponse = OperatorFactory.operatorResponse(operator)

        return OperatorLoginResponse(operatorResponse, token)
    }

    private fun verifyOperatorStatus(operator: Operator) {
        if (operator.status == OperatorStatus.Disabled.name) throw Exception(Errors.AccountNotActivated.description)
        if (operator.status == OperatorStatus.Blocked.name) throw Exception(Errors.BlockedAccount.description)
        return
    }

    private suspend fun registerSession(operatorId: String, sessionId: String) {
        val liveSessions = sessionRepository.getSession(operatorId)
        val liveSession = LiveSession(sessionId, LocalDateTime.now().toString())

        if (liveSessions == null) {
            val updatedLiveSessions = LiveSessions(
                id = sessionId,
                sessions = listOf(liveSession)
            )

            return sessionRepository.setSession(updatedLiveSessions)
        }

        val liveSessionFoundBySession = liveSessions.sessions.find { it.sessionId == sessionId }
        if (liveSessionFoundBySession == null) {
            val liveSessionList = liveSessions.sessions.toMutableList()
            liveSessionList.add(liveSession)

            val updatedLiveSessions = liveSessions.copy(sessions = liveSessionList)

            return sessionRepository.setSession(updatedLiveSessions)
        }
    }

    suspend fun generateValidationEmailToken(email: String) {
        val adminEmail = dotEnv("SUPERADMIN_EMAIL")
        val operator = databaseRepository.getOperatorByEmail(email)

        val tokenInfo = TokenInfo(
            email = operator.email,
            id = operator.id.toString(),
            sessionId = LocalDateTime.now().toString(),
            time = TimeUnit.MINUTES.toMillis(5)
        )

        val token = TokenGenerator.moderator(tokenInfo)

        CoroutineScope(Dispatchers.Default).launch {
            MailNotificationService().sendConfirmationTokenEmail(
                name = operator.name,
                to = adminEmail,
                token = token
            )
        }
    }

    suspend fun activateOperatorByToken(token: String) {
        val secret = dotEnv("OPERATOR_SECRET")

        val verifier = JWT.require(Algorithm.HMAC256(secret)).build()
        val decodedJWT = verifier.verify(token)
        val operatorId = decodedJWT.getClaim("id").asString()

        activateOperatorById(operatorId)
    }

    private suspend fun activateOperatorById(operatorId: String) {
        val update = OperationField(Fields.Status, OperatorStatus.Activated.name)
        databaseRepository.updateOperator(operatorId, update)
    }

    suspend fun session(operatorId: String, sessionId: String): OperatorResponse {
        val operator = databaseRepository.getOperatorById(operatorId)
        return OperatorFactory.operatorResponse(operator)
    }

    suspend fun signOut(operatorId: String, sessionId: String) {
        sessionRepository.revokeSession(sessionId)
    }
}
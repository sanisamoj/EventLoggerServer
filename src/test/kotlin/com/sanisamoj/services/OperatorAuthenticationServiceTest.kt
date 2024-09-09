package com.sanisamoj.services

import com.sanisamoj.ContextTest
import com.sanisamoj.config.GlobalContext
import com.sanisamoj.config.GlobalContext.EMPTY_VALUE
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.dataclass.OperatorLoginRequest
import com.sanisamoj.data.models.dataclass.OperatorLoginResponse
import com.sanisamoj.data.models.dataclass.TokenInfo
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.database.redis.Redis
import com.sanisamoj.services.operator.OperatorAuthenticationService
import com.sanisamoj.utils.eraseAllDataToTests
import com.sanisamoj.utils.generators.TokenGenerator
import io.ktor.server.testing.testApplication
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class OperatorAuthenticationServiceTest {
    private val databaseRepository: DatabaseRepository = GlobalContext.databaseRepository

    init {
        Redis.initialize()
        eraseAllDataToTests()
    }

    @Test
    fun loginTest() = testApplication {
        val operator: Operator = ContextTest.createOperatorTest(OperatorStatus.Activated)
        val operatorAuthenticationService = OperatorAuthenticationService()
        val operatorLoginRequest = OperatorLoginRequest(operator.email, "wrong")
        assertFails { operatorAuthenticationService.login(operatorLoginRequest) }

        val login: OperatorLoginResponse = operatorAuthenticationService.login(operatorLoginRequest.copy(password = ContextTest.operator.password))
        assertEquals(operator.id.toString(), login.account.id)
        assertEquals(operator.name, login.account.name)
        assertEquals(operator.email, login.account.email)
        assertEquals(operator.phone, login.account.phone)
        assertEquals(operator.profileImage, EMPTY_VALUE)

        ContextTest.deleteOperator()
    }

    @Test
    fun generateTokenAndValidationOperator() = testApplication {
        val operatorAuthenticationService = OperatorAuthenticationService(mailRepository = ContextTest.mailRepository)
        val operator: Operator = ContextTest.createOperatorTest(OperatorStatus.Disabled)

        val tokenInfo = TokenInfo(
            email = operator.email,
            id = operator.id.toString(),
            sessionId = LocalDateTime.now().toString(),
            time = TimeUnit.MINUTES.toMillis(5)
        )
        val token: String = TokenGenerator.moderator(tokenInfo)

        assertFails { operatorAuthenticationService.activateOperatorByToken(operator.email) }
        operatorAuthenticationService.activateOperatorByToken(token)
        val updatedOperator: Operator = databaseRepository.getOperatorById(operator.id.toString())
        assertEquals(updatedOperator.id.toString(), operator.id.toString())
        assertEquals(updatedOperator.status, OperatorStatus.Activated.name)

        ContextTest.deleteOperator()
    }
}
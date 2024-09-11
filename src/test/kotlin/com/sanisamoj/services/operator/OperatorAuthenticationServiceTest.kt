package com.sanisamoj.services.operator

import com.sanisamoj.ContextTest
import com.sanisamoj.config.GlobalContext.EMPTY_VALUE
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.dataclass.OperatorLoginRequest
import com.sanisamoj.data.models.dataclass.OperatorLoginResponse
import com.sanisamoj.data.models.dataclass.OperatorResponse
import com.sanisamoj.data.models.dataclass.TokenInfo
import com.sanisamoj.data.models.dataclass.ValidationCode
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.database.redis.Redis
import com.sanisamoj.utils.eraseAllDataToTests
import com.sanisamoj.utils.generators.TokenGenerator
import io.ktor.server.testing.testApplication
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class OperatorAuthenticationServiceTest {
    private val databaseRepository: DatabaseRepository = ContextTest.databaseRepository

    @BeforeTest
    fun initializeRedAndDeleteAllDataToTests() {
        Redis.initialize()
        eraseAllDataToTests()
    }

    @AfterTest
    fun deleteAllDataTests() {
        eraseAllDataToTests()
    }

    @Test
    fun loginTest() = testApplication {
        val operator: Operator = ContextTest.createOperatorTest(OperatorStatus.Activated)
        val operatorAuthenticationService = OperatorAuthenticationService()
        val operatorLoginRequest = OperatorLoginRequest(operator.email, "wrong")
        assertFails { operatorAuthenticationService.login(operatorLoginRequest) }

        val login: OperatorLoginResponse =
            operatorAuthenticationService.login(operatorLoginRequest.copy(password = ContextTest.operator.password))
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

    @Test
    fun generateValidationCodeByWhatsappAndValidation() = testApplication {
        val operatorAuthenticationService = OperatorAuthenticationService(
            mailRepository = ContextTest.mailRepository,
            databaseRepository = ContextTest.databaseRepository
        )

        val operator: Operator = ContextTest.createOperatorTest(OperatorStatus.Disabled)
        operatorAuthenticationService.generateValidationCodeByWhatsapp(operator.email)
        val generatedCode: Int = databaseRepository.getOperatorById(operator.id.toString()).validationCode
        assertFails { operatorAuthenticationService.activateOperatorByValidationCode(ValidationCode(operator.email, 123456)) }
        operatorAuthenticationService.activateOperatorByValidationCode(ValidationCode(operator.email, generatedCode))

        val updatedOperator: Operator = databaseRepository.getOperatorByEmail(operator.email)
        assertEquals(OperatorStatus.Activated.name, updatedOperator.status)
        ContextTest.deleteOperator()
    }

    @Test
    fun sessionTest() = testApplication {
        val operatorAuthenticationService = OperatorAuthenticationService(
            mailRepository = ContextTest.mailRepository,
            databaseRepository = ContextTest.databaseRepository
        )
        val operator: Operator = ContextTest.createOperatorTest(OperatorStatus.Activated)

        val operatorResponse: OperatorResponse = operatorAuthenticationService.session(operator.id.toString())
        assertEquals(operator.id.toString(), operatorResponse.id)
        assertEquals(operator.name, operatorResponse.name)
        assertEquals(operator.email, operatorResponse.email)
        assertEquals(operator.phone, operatorResponse.phone)
        assertEquals(operator.profileImage, operatorResponse.profileImage)
        ContextTest.deleteOperator()
    }
}
package com.sanisamoj.services

import com.sanisamoj.ContextTest
import com.sanisamoj.config.GlobalContext.EMPTY_VALUE
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.dataclass.OperatorLoginRequest
import com.sanisamoj.data.models.dataclass.OperatorLoginResponse
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.database.redis.Redis
import com.sanisamoj.services.operator.OperatorAuthenticationService
import com.sanisamoj.utils.eraseAllDataToTests
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class OperatorAuthenticationServiceTest {

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
}
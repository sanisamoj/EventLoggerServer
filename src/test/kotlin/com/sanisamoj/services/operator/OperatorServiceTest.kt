package com.sanisamoj.services.operator

import com.sanisamoj.ContextTest
import com.sanisamoj.data.models.dataclass.CreateOperatorRequest
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.dataclass.OperatorResponse
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.utils.eraseAllDataToTests
import io.ktor.server.testing.testApplication
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class OperatorServiceTest {
    private val databaseRepository: DatabaseRepository = ContextTest.databaseRepository

    @BeforeTest
    fun deleteAllDataToTests() {
        eraseAllDataToTests()
    }

    @AfterTest
    fun deleteAllDataTests() {
        eraseAllDataToTests()
    }

    @Test
    fun createOperatorTest() = testApplication {
        val operatorService = OperatorService()
        val createOperatorRequest = CreateOperatorRequest(
            name = "OperatorTestName",
            email = "operatorEmail@domain.test",
            phone = "111111111111",
            password = "passwordTest"
        )

        val operatorResponse: OperatorResponse = operatorService.create(createOperatorRequest)
        assertEquals(createOperatorRequest.name, operatorResponse.name)
        assertEquals(createOperatorRequest.email, operatorResponse.email)
        assertEquals(createOperatorRequest.phone, operatorResponse.phone)

        val operatorInDb: Operator = databaseRepository.getOperatorById(operatorResponse.id)
        assertEquals(createOperatorRequest.name, operatorInDb.name)
        assertEquals(createOperatorRequest.email, operatorInDb.email)
        assertEquals(createOperatorRequest.phone, operatorInDb.phone)

        databaseRepository.deleteOperator(operatorInDb.id.toString())
    }

    @Test
    fun deleteOperatorTest() = testApplication {
        val operatorService = OperatorService()
        val createOperatorRequest = CreateOperatorRequest(
            name = "OperatorTestName",
            email = "operatorEmail@domain.test",
            phone = "111111111111",
            password = "passwordTest"
        )

        val operatorResponse: OperatorResponse = operatorService.create(createOperatorRequest)
        val operatorInDb: Operator = databaseRepository.getOperatorById(operatorResponse.id)
        operatorService.delete(operatorInDb.id.toString())
        assertFails { databaseRepository.getOperatorById(operatorInDb.id.toString()) }
    }

    @Test
    fun blockOperatorTest() = testApplication {
        val operatorService = OperatorService()
        val createOperatorRequest = CreateOperatorRequest(
            name = "OperatorTestName",
            email = "operatorEmail@domain.test",
            phone = "111111111111",
            password = "passwordTest"
        )

        val operatorResponse: OperatorResponse = operatorService.create(createOperatorRequest)
        assertEquals(createOperatorRequest.name, operatorResponse.name)
        assertEquals(createOperatorRequest.email, operatorResponse.email)
        assertEquals(createOperatorRequest.phone, operatorResponse.phone)

        operatorService.blockOperatorByEmail(operatorResponse.email)
        val operatorInDb: Operator = databaseRepository.getOperatorById(operatorResponse.id)
        assertEquals(createOperatorRequest.name, operatorInDb.name)
        assertEquals(createOperatorRequest.email, operatorInDb.email)
        assertEquals(createOperatorRequest.phone, operatorInDb.phone)
        assertEquals(OperatorStatus.Blocked.name, operatorInDb.status)

        databaseRepository.deleteOperator(operatorResponse.id)
    }
}
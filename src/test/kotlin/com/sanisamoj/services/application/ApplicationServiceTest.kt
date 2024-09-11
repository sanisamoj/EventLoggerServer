package com.sanisamoj.services.application

import com.sanisamoj.ContextTest
import com.sanisamoj.data.models.dataclass.ApplicationServiceLoginRequest
import com.sanisamoj.data.models.dataclass.ApplicationServiceResponse
import com.sanisamoj.data.models.dataclass.CreateApplicationServiceRequest
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.database.redis.Redis
import com.sanisamoj.utils.eraseAllDataToTests
import io.ktor.server.testing.testApplication
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ApplicationServiceTest {
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
    fun createApplicationTest() = testApplication {
        val applicationService = ApplicationService(databaseRepository = ContextTest.databaseRepository)
        val createApplicationServiceTest = CreateApplicationServiceRequest(
            applicationName = "ApplicationTest",
            description = "Application Test Description",
            password = "PasswordTest"
        )
        val applicationServiceResponse: ApplicationServiceResponse = applicationService.create(createApplicationServiceTest)
        assertEquals(createApplicationServiceTest.applicationName, applicationServiceResponse.applicationName)
        assertEquals(createApplicationServiceTest.description, applicationServiceResponse.description)

        databaseRepository.deleteApplicationServiceById(applicationServiceResponse.id)
    }

    @Test
    fun loginApplicationTest() = testApplication {
        val applicationService = ApplicationService(databaseRepository = ContextTest.databaseRepository)
        val applicationServiceResponse: ApplicationServiceResponse = applicationService.create(ContextTest.createApplicationService)

        assertFails { applicationService.login(ApplicationServiceLoginRequest(applicationServiceResponse.applicationName, "wrongPassword")) }
        databaseRepository.deleteApplicationServiceById(applicationServiceResponse.id)
    }

    @Test
    fun getAllApplicationsTest() = testApplication {
        val applicationService = ApplicationService(databaseRepository = ContextTest.databaseRepository)
        val applicationServiceResponse: ApplicationServiceResponse = applicationService.create(ContextTest.createApplicationService)

        val applicationServiceResponseList:  List<ApplicationServiceResponse> = applicationService.getAllApplicationService()
        assertEquals(1, applicationServiceResponseList.size)
        databaseRepository.deleteApplicationServiceById(applicationServiceResponse.id)
    }

    @Test
    fun deleteApplicationTest() = testApplication {
        val applicationService = ApplicationService(databaseRepository = ContextTest.databaseRepository)
        val applicationServiceResponse: ApplicationServiceResponse = applicationService.create(ContextTest.createApplicationService)
        applicationService.deleteApplicationServiceById(applicationServiceResponse.id)

        assertEquals(0, applicationService.getAllApplicationService().size)
    }

}
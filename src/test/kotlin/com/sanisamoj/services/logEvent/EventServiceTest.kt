package com.sanisamoj.services.logEvent

import com.sanisamoj.ContextTest
import com.sanisamoj.data.models.dataclass.ApplicationServiceResponse
import com.sanisamoj.data.models.dataclass.CreateEventRequest
import com.sanisamoj.data.models.dataclass.EventLoggerFilter
import com.sanisamoj.data.models.dataclass.LogEvent
import com.sanisamoj.data.models.dataclass.LogEventResponse
import com.sanisamoj.data.models.dataclass.LogEventWithPaginationResponse
import com.sanisamoj.data.models.enums.EventSeverity
import com.sanisamoj.data.models.enums.EventType
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.database.redis.Redis
import com.sanisamoj.utils.eraseAllDataToTests
import io.ktor.server.testing.testApplication
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EventServiceTest {

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
    fun registerEventTest() = testApplication {
        val eventServiceTest: EventService = EventService(databaseRepository = databaseRepository)
        val applicationServiceResponse: ApplicationServiceResponse = ContextTest.createApplication()

        val fakeEventRequest: CreateEventRequest = ContextTest.fakeEventRequest.copy(id = applicationServiceResponse.id)

        val logEventResponse: LogEventResponse = eventServiceTest.registerEvent(fakeEventRequest)

        assertEquals(fakeEventRequest.id, applicationServiceResponse.id)
        assertEquals(fakeEventRequest.serviceName, logEventResponse.serviceName)
        assertEquals(fakeEventRequest.eventType, logEventResponse.eventType)
        assertEquals(fakeEventRequest.errorCode, logEventResponse.errorCode)
        assertEquals(fakeEventRequest.message, logEventResponse.message)
        assertEquals(fakeEventRequest.description, logEventResponse.description)
        assertEquals(fakeEventRequest.severity, logEventResponse.severity)
        assertEquals(fakeEventRequest.stackTrace, logEventResponse.stackTrace)
        assertEquals(fakeEventRequest.additionalData, logEventResponse.additionalData)

        databaseRepository.deleteEventById(logEventResponse.id)
    }

    @Test
    fun deleteEventTest() = testApplication {
        val eventServiceTest: EventService = EventService(databaseRepository = databaseRepository)
        val applicationServiceResponse: ApplicationServiceResponse = ContextTest.createApplication()

        val fakeEventRequest: CreateEventRequest = ContextTest.fakeEventRequest.copy(id = applicationServiceResponse.id)
        val logEventResponse: LogEventResponse = eventServiceTest.registerEvent(fakeEventRequest)

        eventServiceTest.deleteEvent(logEventResponse.id)
        val logEventWithPaginationResponse: LogEventWithPaginationResponse = eventServiceTest.getAllEvents(false, 1, 10)
        assertEquals(0, logEventWithPaginationResponse.content.size)
    }

    @Test
    fun readEventTest() = testApplication {
        val eventServiceTest: EventService = EventService(databaseRepository = databaseRepository)
        val applicationServiceResponse: ApplicationServiceResponse = ContextTest.createApplication()

        val fakeEventRequest: CreateEventRequest = ContextTest.fakeEventRequest.copy(id = applicationServiceResponse.id)
        val logEventResponse: LogEventResponse = eventServiceTest.registerEvent(fakeEventRequest)

        eventServiceTest.readEvent(logEventResponse.id, true)
        var updatedLogEvent: LogEvent = databaseRepository.getEventById(logEventResponse.id)

        assertEquals(logEventResponse.id, updatedLogEvent.id.toString())
        assertEquals(fakeEventRequest.serviceName, updatedLogEvent.serviceName)
        assertEquals(fakeEventRequest.eventType, updatedLogEvent.eventType)
        assertEquals(fakeEventRequest.errorCode, updatedLogEvent.errorCode)
        assertEquals(fakeEventRequest.message, updatedLogEvent.message)
        assertEquals(fakeEventRequest.description, updatedLogEvent.description)
        assertEquals(fakeEventRequest.severity, updatedLogEvent.severity)
        assertEquals(fakeEventRequest.stackTrace, updatedLogEvent.stackTrace)
        assertEquals(fakeEventRequest.additionalData, updatedLogEvent.additionalData)
        assertEquals(true, updatedLogEvent.read)

        eventServiceTest.readEvent(logEventResponse.id, false)
        updatedLogEvent = databaseRepository.getEventById(logEventResponse.id)

        assertEquals(logEventResponse.id, updatedLogEvent.id.toString())
        assertEquals(fakeEventRequest.serviceName, updatedLogEvent.serviceName)
        assertEquals(fakeEventRequest.eventType, updatedLogEvent.eventType)
        assertEquals(fakeEventRequest.errorCode, updatedLogEvent.errorCode)
        assertEquals(fakeEventRequest.message, updatedLogEvent.message)
        assertEquals(fakeEventRequest.description, updatedLogEvent.description)
        assertEquals(fakeEventRequest.severity, updatedLogEvent.severity)
        assertEquals(fakeEventRequest.stackTrace, updatedLogEvent.stackTrace)
        assertEquals(fakeEventRequest.additionalData, updatedLogEvent.additionalData)
        assertEquals(false, updatedLogEvent.read)

        databaseRepository.deleteEventById(logEventResponse.id)
    }

    @Test
    fun getAllEventsTest() = testApplication {
        val eventServiceTest: EventService = EventService(databaseRepository = databaseRepository)
        val applicationServiceResponse: ApplicationServiceResponse = ContextTest.createApplication()

        val fakeEventRequest: CreateEventRequest = ContextTest.fakeEventRequest.copy(id = applicationServiceResponse.id)
        eventServiceTest.registerEvent(fakeEventRequest)
        eventServiceTest.registerEvent(fakeEventRequest)
        eventServiceTest.registerEvent(fakeEventRequest)
        eventServiceTest.registerEvent(fakeEventRequest)

        val logEventWithPaginationResponse: LogEventWithPaginationResponse = eventServiceTest.getAllEvents(false, 1, 10)
        assertEquals(4, logEventWithPaginationResponse.content.size)
        deleteAllDataTests()
    }

    @Test
    fun getAllEventsByFilterTest() = testApplication {
        val eventServiceTest: EventService = EventService(databaseRepository = databaseRepository)
        val applicationServiceResponse: ApplicationServiceResponse = ContextTest.createApplication()

        val filter = EventLoggerFilter(
            eventType = EventType.ERROR.name,
            eventSeverity = EventSeverity.HIGH.name,
            ordering = -1,
            read = false,
            page = 1,
            size = 10
        )

        val fakeEventRequest1 = ContextTest.fakeEventRequest.copy(id = applicationServiceResponse.id)
        val fakeEventRequest2 = ContextTest.fakeEventRequest.copy(id = applicationServiceResponse.id, message = "Different message")
        val loginResponse1: LogEventResponse = eventServiceTest.registerEvent(fakeEventRequest1)
        val loginResponse2: LogEventResponse = eventServiceTest.registerEvent(fakeEventRequest2)

        val logEventWithPaginationResponse = eventServiceTest.getAllEventsByFilter(filter)

        assertEquals(2, logEventWithPaginationResponse.content.size)

        val eventResponse1 = logEventWithPaginationResponse.content.find { it.id == loginResponse1.id }
        val eventResponse2 = logEventWithPaginationResponse.content.find { it.id == loginResponse2.id }

        assertEquals(loginResponse1.id, eventResponse1?.id)
        assertEquals(loginResponse2.id, eventResponse2?.id)
        assertEquals(EventType.ERROR.name, eventResponse1?.eventType)
        assertEquals(EventSeverity.HIGH.name, eventResponse1?.severity)
        assertEquals("Failed login attempt for user.", eventResponse1?.message)
        assertEquals("The user with ID 12345 attempted to log in but failed due to incorrect password.", eventResponse1?.description)

        assertEquals(EventType.ERROR.name, eventResponse2?.eventType)
        assertEquals(EventSeverity.HIGH.name, eventResponse2?.severity)
        assertEquals("Different message", eventResponse2?.message)
        assertEquals("The user with ID 12345 attempted to log in but failed due to incorrect password.", eventResponse2?.description)
    }
}


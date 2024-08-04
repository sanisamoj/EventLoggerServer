package com.sanisamoj.services.logEvent

import com.sanisamoj.context.GlobalContext
import com.sanisamoj.data.models.dataclass.EventLoggerFilter
import com.sanisamoj.data.models.dataclass.LogEvent
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.dataclass.CreateEventRequest
import com.sanisamoj.data.models.dataclass.LogEventResponse
import com.sanisamoj.data.models.dataclass.LogEventWithPaginationResponse
import com.sanisamoj.database.mongodb.Fields
import com.sanisamoj.database.mongodb.OperationField
import com.sanisamoj.utils.pagination.PaginationResponse
import com.sanisamoj.utils.pagination.paginationMethod

class EventService(private val databaseRepository: DatabaseRepository = GlobalContext.databaseRepository) {
    suspend fun registerEvent(createEventRequest: CreateEventRequest): LogEventResponse {
        val logEvent = databaseRepository.registerEvent(createEventRequest)
        return LogEventFactory.logEventResponse(logEvent)
    }

    suspend fun deleteEvent(eventId: String) {
        databaseRepository.deleteEventById(eventId)
    }

    suspend fun readEvent(eventId: String, read: Boolean = true) {
        val update = OperationField(Fields.Read, read)
        databaseRepository.updateEvent(eventId, update)
    }

    suspend fun getAllEventsByFilter(filter: EventLoggerFilter): LogEventWithPaginationResponse {
        val logEvents = databaseRepository.getAllEventsByFilter(filter)
        val logEventsResponse: MutableList<LogEventResponse> = mutableListOf()
        logEvents.forEach {
            logEventsResponse.add(LogEventFactory.logEventResponse(it))
        }

        val totalItems = databaseRepository.countAllEventsByFilter(filter).toDouble()
        val paginationResponse = paginationMethod(totalItems, filter.size, filter.page)
        val logEventsWithPaginationResponse = LogEventWithPaginationResponse(
            content = logEventsResponse,
            paginationResponse = paginationResponse
        )

        return logEventsWithPaginationResponse
    }

    suspend fun getAllEvents(read: Boolean? = null, page: Int, size: Int): LogEventWithPaginationResponse {
        lateinit var logEvents: List<LogEvent>
        lateinit var paginationResponse: PaginationResponse

        if(read != null) {
            logEvents = databaseRepository.getAllEventsByRead(read, page, size)
            val totalItems = databaseRepository.countAllEventsByRead(read).toDouble()
            paginationResponse = paginationMethod(totalItems, size, page)
        } else {
            val totalItems = databaseRepository.countAllEvents().toDouble()
            paginationResponse = paginationMethod(totalItems, size, page)
            logEvents = databaseRepository.getAllEvents(page, size)
        }

        val logEventsResponse: MutableList<LogEventResponse> = mutableListOf()
        logEvents.forEach {
            logEventsResponse.add(LogEventFactory.logEventResponse(it))
        }

        val logEventsWithPaginationResponse = LogEventWithPaginationResponse(
            content = logEventsResponse,
            paginationResponse = paginationResponse
        )

        return logEventsWithPaginationResponse
    }
}
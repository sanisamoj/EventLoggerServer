package com.sanisamoj.data.models.interfaces

import com.sanisamoj.data.models.dataclass.*
import com.sanisamoj.database.mongodb.OperationField

interface DatabaseRepository {
    suspend fun createOperator(operator: Operator): Operator
    suspend fun deleteOperator(operatorId: String)
    suspend fun getOperatorById(operatorId: String): Operator
    suspend fun getOperatorByEmail(email: String): Operator
    suspend fun getOperatorByName(name: String): Operator
    suspend fun getOperatorByPhone(phone: String): Operator
    suspend fun updateOperator(operatorId: String, update: OperationField): Operator
    suspend fun generateValidationCode(identification: String, code: Int? = null)

    suspend fun registerEvent(logEvent: CreateEventRequest): LogEvent
    suspend fun deleteEventById(eventId: String)
    suspend fun getEventById(eventId: String): LogEvent
    suspend fun getAllEventsByFilter(eventLoggerFilter: EventLoggerFilter): List<LogEvent>
    suspend fun getAllEventsByRead(read: Boolean, page: Int, size: Int): List<LogEvent>
    suspend fun getAllEvents(page: Int, size: Int): List<LogEvent>
    suspend fun countAllEventsByRead(read: Boolean): Int
    suspend fun countAllEvents(): Int
    suspend fun countAllEventsByFilter(eventLoggerFilter: EventLoggerFilter): Int
    suspend fun updateEvent(eventId: String, update: OperationField): LogEvent

    suspend fun createApplicationService(name: String, description: String, password: String): ApplicationServiceData
    suspend fun deleteApplicationServiceById(applicationId: String)
    suspend fun getApplicationServiceById(applicationId: String): ApplicationServiceData
    suspend fun getApplicationServiceByName(applicationName: String): ApplicationServiceData
    suspend fun updateApplicationService(applicationId: String, update: OperationField): ApplicationServiceData
    suspend fun getAllApplicationServices(): List<ApplicationServiceData>

    suspend fun getAllBots(): List<Bot>
    suspend fun registerBot(bot: Bot): Bot
}
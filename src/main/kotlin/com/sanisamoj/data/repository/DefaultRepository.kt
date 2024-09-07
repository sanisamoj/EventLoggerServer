package com.sanisamoj.data.repository

import com.sanisamoj.config.GlobalContext.EMPTY_VALIDATION_CODE
import com.sanisamoj.config.GlobalContext.botRepository
import com.sanisamoj.config.GlobalContext.errorMessages
import com.sanisamoj.config.GlobalContext.warningMessagesToChat
import com.sanisamoj.data.models.dataclass.*
import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.database.mongodb.CollectionsInDb
import com.sanisamoj.database.mongodb.Fields
import com.sanisamoj.database.mongodb.MongodbOperations
import com.sanisamoj.database.mongodb.OperationField
import com.sanisamoj.utils.analyzers.dotEnv
import com.sanisamoj.utils.generators.CharactersGenerator
import io.ktor.server.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DefaultRepository : DatabaseRepository {
    override suspend fun createOperator(operator: Operator): Operator {
        val operatorId = MongodbOperations().register(CollectionsInDb.Operators, operator).toString()
        return getOperatorById(operatorId)
    }

    override suspend fun deleteOperator(operatorId: String) {
        MongodbOperations().deleteItem<Operator>(
            collectionName = CollectionsInDb.Operators,
            filter = OperationField(Fields.Id, ObjectId(operatorId))
        )
    }

    override suspend fun getOperatorById(operatorId: String): Operator {
        return MongodbOperations().findOne<Operator>(
            collectionName = CollectionsInDb.Operators,
            filter = OperationField(Fields.Id, ObjectId(operatorId))
        ) ?: throw NotFoundException(errorMessages.accountNotExists)
    }

    override suspend fun getOperatorByEmail(email: String): Operator {
        return MongodbOperations().findOne<Operator>(
            collectionName = CollectionsInDb.Operators,
            filter = OperationField(Fields.Email, email)
        ) ?: throw NotFoundException(errorMessages.accountNotExists)
    }

    override suspend fun getOperatorByName(name: String): Operator {
        return MongodbOperations().findOne<Operator>(
            collectionName = CollectionsInDb.Operators,
            filter = OperationField(Fields.Name, name)
        ) ?: throw NotFoundException(errorMessages.accountNotExists)
    }

    override suspend fun getOperatorByPhone(phone: String): Operator {
        return MongodbOperations().findOne<Operator>(
            collectionName = CollectionsInDb.Operators,
            filter = OperationField(Fields.Phone, phone)
        ) ?: throw NotFoundException(errorMessages.accountNotExists)
    }

    override suspend fun updateOperator(operatorId: String, update: OperationField): Operator {
        return MongodbOperations().updateAndReturnItem<Operator>(
            collectionName = CollectionsInDb.Operators,
            filter = OperationField(Fields.Id, ObjectId(operatorId)),
            update = update
        ) ?: throw NotFoundException(Errors.AccountNotExists.description)
    }

    override suspend fun generateValidationCode(identification: String, code: Int?) {
        val operator: Operator = if (identification.any { it.isLetter() }) {
            getOperatorByEmail(identification)
        } else {
            getOperatorByPhone(identification)
        }

        val validationCode = code ?: CharactersGenerator().codeValidationGenerate()
        val update = OperationField(Fields.ValidationCode, validationCode)
        updateOperator(operator.id.toString(), update)
        deleteValidationCodeInSpecificTime(operator.id.toString())

        CoroutineScope(Dispatchers.IO).launch {
            sendValidationCodeToTheUser(dotEnv("SUPERADMIN_PHONE"), validationCode)
        }
    }

    private fun deleteValidationCodeInSpecificTime(operatorId: String, timeInMinutes: Long = 5) {
        val executorService = Executors.newSingleThreadScheduledExecutor()
        executorService.schedule({
            runBlocking {
                val update = OperationField(Fields.ValidationCode, EMPTY_VALIDATION_CODE)
                updateOperator(operatorId, update)
            }
        }, timeInMinutes, TimeUnit.MINUTES)

        return
    }

    private suspend fun sendValidationCodeToTheUser(userPhone: String, validationCode: Int) {
        val firstMessage = "${warningMessagesToChat.yourVerificationCodeIs} $validationCode"
        val secondMessage: String = warningMessagesToChat.doNotShareThisCode
        val thirdMessage = "$validationCode"

        botRepository.sendMessage(MessageToSend(userPhone, firstMessage))
        botRepository.sendMessage(MessageToSend(userPhone, secondMessage))
        botRepository.sendMessage(MessageToSend(userPhone, thirdMessage))
    }

    override suspend fun registerEvent(logEvent: CreateEventRequest): LogEvent {
        val log: LogEvent = logEventFactory(logEvent)
        val id = MongodbOperations().register(CollectionsInDb.LogEvent, log).toString()
        return getEventById(id)
    }

    private suspend fun logEventFactory(logEvent: CreateEventRequest): LogEvent {
        val applicationService = getApplicationServiceById(logEvent.id)
        val count = countAllEvents()
        return LogEvent(
            applicationId = logEvent.id,
            number = count.inc(),
            applicationName = applicationService.applicationName,
            serviceName = logEvent.serviceName,
            eventType = logEvent.eventType,
            errorCode = logEvent.errorCode,
            message = logEvent.message,
            description = logEvent.description,
            severity = logEvent.severity,
            stackTrace = logEvent.stackTrace,
            additionalData = logEvent.additionalData,
            read = false
        )
    }

    override suspend fun deleteEventById(eventId: String) {
        MongodbOperations().deleteItem<LogEvent>(
            collectionName = CollectionsInDb.LogEvent,
            filter = OperationField(Fields.Id, ObjectId(eventId))
        )
    }

    override suspend fun getEventById(eventId: String): LogEvent {
        return MongodbOperations().findOne<LogEvent>(
            collectionName = CollectionsInDb.LogEvent,
            filter = OperationField(Fields.Id, ObjectId(eventId))
        ) ?: throw NotFoundException(Errors.LogEventNotExists.description)
    }

    override suspend fun getAllEventsByFilter(eventLoggerFilter: EventLoggerFilter): List<LogEvent> {
        val additionalFilters = listOf(
            OperationField(Fields.Severity, eventLoggerFilter.eventSeverity),
            OperationField(Fields.Read, eventLoggerFilter.read)
        )

        return MongodbOperations().findAllByFilterWithPagingBySorting(
            collectionName = CollectionsInDb.LogEvent,
            filter = OperationField(Fields.EventType, eventLoggerFilter.eventType),
            additionalFilters = additionalFilters,
            sortingFilter = OperationField(Fields.Number, eventLoggerFilter.ordering),
            pageNumber = eventLoggerFilter.page,
            pageSize = eventLoggerFilter.size
        )
    }

    override suspend fun getAllEventsByRead(read: Boolean, page: Int, size: Int): List<LogEvent> {
        return MongodbOperations().findAllByFilterWithPagingBySorting(
            collectionName = CollectionsInDb.LogEvent,
            sortingFilter = OperationField(Fields.Number, -1),
            filter = OperationField(Fields.Read, read),
            pageSize = size,
            pageNumber = page
        )
    }

    override suspend fun getAllEvents(page: Int, size: Int): List<LogEvent> {
        return MongodbOperations().findAllWithPagingBySorting(
            collectionName = CollectionsInDb.LogEvent,
            sortingFilter = OperationField(Fields.Number, -1),
            pageSize = size,
            pageNumber = page
        )
    }

    override suspend fun countAllEventsByRead(read: Boolean): Int {
        return MongodbOperations().countDocuments<LogEvent>(
            collectionName = CollectionsInDb.LogEvent,
            filter = OperationField(Fields.Read, read)
        )
    }

    override suspend fun countAllEvents(): Int {
        return MongodbOperations().countDocumentsWithoutFilter<LogEvent>(CollectionsInDb.LogEvent)
    }

    override suspend fun countAllEventsByFilter(eventLoggerFilter: EventLoggerFilter): Int {
        val additionalFilters = listOf(
            OperationField(Fields.Severity, eventLoggerFilter.eventSeverity),
            OperationField(Fields.Read, eventLoggerFilter.read)
        )

        return MongodbOperations().countDocuments<LogEvent>(
            collectionName = CollectionsInDb.LogEvent,
            filter = OperationField(Fields.EventType, eventLoggerFilter.eventType),
            additionalFilters = additionalFilters,
        )
    }

    override suspend fun updateEvent(eventId: String, update: OperationField): LogEvent {
        return MongodbOperations().updateAndReturnItem<LogEvent>(
            collectionName = CollectionsInDb.LogEvent,
            filter = OperationField(Fields.Id, ObjectId(eventId)),
            update = update
        ) ?: throw NotFoundException(Errors.LogEventNotExists.description)
    }

    override suspend fun createApplicationService(
        name: String,
        description: String,
        password: String
    ): ApplicationServiceData {

        val id = MongodbOperations().register(
            collectionInDb = CollectionsInDb.ApplicationServices,
            item = ApplicationServiceData(
                applicationName = name,
                description = description,
                password = password
            )
        ).toString()

        return getApplicationServiceById(id)
    }

    override suspend fun deleteApplicationServiceById(applicationId: String) {
        MongodbOperations().deleteItem<ApplicationServiceData>(
            collectionName = CollectionsInDb.ApplicationServices,
            filter = OperationField(Fields.Id, ObjectId(applicationId))
        )
    }

    override suspend fun getApplicationServiceById(applicationId: String): ApplicationServiceData {
        return MongodbOperations().findOne<ApplicationServiceData>(
            collectionName = CollectionsInDb.ApplicationServices,
            filter = OperationField(Fields.Id, ObjectId(applicationId))
        ) ?: throw NotFoundException(Errors.ApplicationNotExists.description)
    }

    override suspend fun getApplicationServiceByName(applicationName: String): ApplicationServiceData {
        return MongodbOperations().findOne<ApplicationServiceData>(
            collectionName = CollectionsInDb.ApplicationServices,
            filter = OperationField(Fields.ApplicationName, applicationName)
        ) ?: throw NotFoundException(Errors.ApplicationNotExists.description)
    }

    override suspend fun updateApplicationService(
        applicationId: String,
        update: OperationField
    ): ApplicationServiceData {
        return MongodbOperations().updateAndReturnItem<ApplicationServiceData>(
            collectionName = CollectionsInDb.ApplicationServices,
            filter = OperationField(Fields.Id, ObjectId(applicationId)),
            update = update
        ) ?: throw NotFoundException(Errors.ApplicationNotExists.description)
    }

    override suspend fun getAllApplicationServices(): List<ApplicationServiceData> {
        return MongodbOperations().findAll(CollectionsInDb.ApplicationServices)
    }

    override suspend fun getAllBots(): List<Bot> {
        return MongodbOperations().findAll(CollectionsInDb.Bots)
    }

    override suspend fun registerBot(bot: Bot): Bot {
        val botId: String = MongodbOperations().register(CollectionsInDb.Bots, bot).toString()
        return getBotById(botId)
    }

    private suspend fun getBotById(id: String): Bot {
        return MongodbOperations().findOne<Bot>(
            collectionName = CollectionsInDb.Bots,
            filter = OperationField(Fields.Id, ObjectId(id))
        ) ?: throw Exception(Errors.BotDoesNotExists.description)
    }
}
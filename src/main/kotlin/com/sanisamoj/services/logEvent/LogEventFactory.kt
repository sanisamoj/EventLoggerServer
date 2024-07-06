package com.sanisamoj.services.logEvent

import com.sanisamoj.data.models.generics.LogEvent
import com.sanisamoj.data.models.responses.LogEventResponse

object LogEventFactory {
    fun logEventResponse(logEvent: LogEvent): LogEventResponse {
        return LogEventResponse(
            id = logEvent.id.toString(),
            applicationId = logEvent.applicationId,
            number = logEvent.number,
            applicationName = logEvent.applicationName,
            serviceName = logEvent.serviceName,
            eventType = logEvent.eventType,
            errorCode = logEvent.errorCode,
            message = logEvent.message,
            description = logEvent.description,
            severity = logEvent.severity,
            stackTrace = logEvent.stackTrace,
            additionalData = logEvent.additionalData,
            read = logEvent.read,
            timestamp = logEvent.timestamp.toString()
        )
    }
}
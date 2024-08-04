package com.sanisamoj.data.models.dataclass

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class LogEvent(
    @BsonId val id: ObjectId = ObjectId(),
    val number: Int,
    val applicationId: String,
    val applicationName: String,
    val serviceName: String? = null,
    val eventType: String,
    val errorCode: String? = null,
    val message: String,
    val description: String? = null,
    val severity: String,
    val stackTrace: String? = null,
    val additionalData: Map<String, String>? = null,
    val read: Boolean,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
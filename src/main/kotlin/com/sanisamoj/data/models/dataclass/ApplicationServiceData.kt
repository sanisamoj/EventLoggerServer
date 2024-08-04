package com.sanisamoj.data.models.dataclass

import com.sanisamoj.context.GlobalContext.EMPTY_VALUE
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class ApplicationServiceData(
    @BsonId val id: ObjectId = ObjectId(),
    val applicationName: String,
    val description: String = EMPTY_VALUE,
    val password: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

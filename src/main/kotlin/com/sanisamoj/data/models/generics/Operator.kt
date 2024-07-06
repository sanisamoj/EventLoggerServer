package com.sanisamoj.data.models.generics

import com.sanisamoj.context.GlobalContext.EMPTY_VALIDATION_CODE
import com.sanisamoj.context.GlobalContext.EMPTY_VALUE
import com.sanisamoj.data.models.enums.OperatorStatus
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Operator(
    @BsonId val id: ObjectId = ObjectId(),
    val name: String,
    val email: String,
    val phone: String,
    val profileImage: String = EMPTY_VALUE,
    val password: String,
    val status: String = OperatorStatus.Disabled.name,
    val validationCode: Int = EMPTY_VALIDATION_CODE,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

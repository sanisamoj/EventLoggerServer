package com.sanisamoj.data.models.dataclass

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class Bot(
    @BsonId
    val id: ObjectId,
    val name: String,
    val description: String,
    val number: String,
    val profileImageUrl: String,
    val groupsId: List<String>,
    val config: NotifyBotConfig = NotifyBotConfig(),
    val createdAt: String = LocalDateTime.now().toString()
)

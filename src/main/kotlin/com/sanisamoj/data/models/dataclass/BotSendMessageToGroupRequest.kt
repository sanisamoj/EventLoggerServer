package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class BotSendMessageToGroupRequest(
    val groupId: String,
    val message: String
)
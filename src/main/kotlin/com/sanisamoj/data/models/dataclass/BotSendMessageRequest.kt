package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class BotSendMessageRequest(
    val phone: String,
    val message: String
)
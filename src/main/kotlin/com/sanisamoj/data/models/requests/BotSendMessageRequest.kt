package com.sanisamoj.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class BotSendMessageRequest(
    val phone: String,
    val message: String
)
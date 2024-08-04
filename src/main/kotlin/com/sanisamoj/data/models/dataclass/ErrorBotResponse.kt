package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBotResponse(
    val statusCode: Int,
    val error: String,
    val details: String? = null
)
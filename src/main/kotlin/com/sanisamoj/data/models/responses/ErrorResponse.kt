package com.sanisamoj.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String,
    val details: String? = null
)

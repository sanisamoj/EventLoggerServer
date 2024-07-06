package com.sanisamoj.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class ValidationCodeRequest (
    val email: String,
    val code: Int
)

package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class ValidationCodeRequest (
    val email: String,
    val code: Int
)

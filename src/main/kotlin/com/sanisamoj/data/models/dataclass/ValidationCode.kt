package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class ValidationCode(
    val email: String,
    val validationCode: Int
)

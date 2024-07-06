package com.sanisamoj.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateOperatorRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)

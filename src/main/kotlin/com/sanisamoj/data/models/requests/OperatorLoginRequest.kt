package com.sanisamoj.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class OperatorLoginRequest (
    val email: String,
    val password: String
)
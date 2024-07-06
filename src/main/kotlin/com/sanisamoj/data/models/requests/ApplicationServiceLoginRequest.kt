package com.sanisamoj.data.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationServiceLoginRequest(
    val applicationName: String,
    val password: String
)
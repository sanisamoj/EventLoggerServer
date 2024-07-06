package com.sanisamoj.data.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class OperatorLoginResponse(
    val account: OperatorResponse,
    val token: String
)

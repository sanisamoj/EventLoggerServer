package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class OperatorLoginResponse(
    val account: OperatorResponse,
    val token: String
)

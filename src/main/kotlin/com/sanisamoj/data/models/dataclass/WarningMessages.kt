package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class WarningMessages(
    val yourVerificationCodeIs: String,
    val doNotShareThisCode: String,
    val botDescription: String
)
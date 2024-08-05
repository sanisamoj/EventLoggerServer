package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class WarningMessages(
    val yourVerificationCodeIs: String = "Your verification code is:",
    val doNotShareThisCode: String = "Do not share this code with anyone. It's only 5 minutes long"
)
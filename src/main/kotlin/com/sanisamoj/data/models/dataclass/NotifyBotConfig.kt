package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class NotifyBotConfig(
    val automaticMessagePermission: Boolean? = false,
    val automaticMessage: String? = null,
    val callPermission: Boolean? = false,
    val automaticCallMessage: String? = null
)
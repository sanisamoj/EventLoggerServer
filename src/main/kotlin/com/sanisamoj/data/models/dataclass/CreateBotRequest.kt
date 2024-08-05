package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class CreateBotRequest(
    val name: String,
    val description: String,
    val profileImage: String,
    val admins: List<String>,
    val config: NotifyBotConfig? = null
)

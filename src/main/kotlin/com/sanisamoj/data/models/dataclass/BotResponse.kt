package com.sanisamoj.data.models.dataclass

import com.sanisamoj.config.GlobalContext.EMPTY_VALUE
import kotlinx.serialization.Serializable

@Serializable
data class BotResponse(
    val id: String,
    val name: String,
    val description: String,
    val number: String,
    val profileImageUrl: String,
    val qrCode: String = EMPTY_VALUE,
    val groups: List<GroupResponse>,
    val config: NotifyBotConfig? = null,
    val active: Boolean,
    val createdAt: String
)
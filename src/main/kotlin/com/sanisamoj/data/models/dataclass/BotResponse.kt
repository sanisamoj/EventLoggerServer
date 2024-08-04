package com.sanisamoj.data.models.dataclass

import com.sanisamoj.context.GlobalContext.EMPTY_VALUE
import kotlinx.serialization.Serializable

@Serializable
data class BotResponse(
    val id: String,
    val name: String,
    val description: String,
    val number: String,
    val profileImage: String,
    val qrCode: String = EMPTY_VALUE
)
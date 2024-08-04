package com.sanisamoj.data.models.dataclass

import com.sanisamoj.context.GlobalContext.EMPTY_VALUE
import kotlinx.serialization.Serializable

@Serializable
data class OperatorResponse(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val profileImage: String = EMPTY_VALUE,
    val createdAt: String
)

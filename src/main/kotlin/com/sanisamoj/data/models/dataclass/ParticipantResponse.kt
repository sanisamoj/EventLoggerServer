package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class ParticipantResponse(
    val phone: String,
    val isAdmin: Boolean
)

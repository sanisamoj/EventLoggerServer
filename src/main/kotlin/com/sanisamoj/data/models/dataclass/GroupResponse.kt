package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class GroupResponse(
    val id: String,
    val title: String,
    val description: String,
    val participants: List<ParticipantResponse>
)
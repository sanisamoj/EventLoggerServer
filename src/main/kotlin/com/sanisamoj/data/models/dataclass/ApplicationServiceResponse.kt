package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationServiceResponse(
    val id: String,
    val applicationName: String,
    val description: String,
    val createdAt: String
)
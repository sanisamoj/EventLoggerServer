package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordData(
    val applicationId: String,
    val password: String
)

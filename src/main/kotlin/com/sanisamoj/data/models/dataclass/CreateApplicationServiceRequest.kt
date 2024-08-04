package com.sanisamoj.data.models.dataclass

import com.sanisamoj.context.GlobalContext.EMPTY_VALUE
import kotlinx.serialization.Serializable

@Serializable
data class CreateApplicationServiceRequest(
    val applicationName: String,
    val description: String = EMPTY_VALUE,
    val password: String,
)
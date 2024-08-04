package com.sanisamoj.data.models.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class VersionResponse(
    val serverVersion: String, // Versão do servidor
    val frontMinVersion: String, // Versão mínima que o frontend deve estar
    val frontTargetVersion: String // Versão mais atual do frontend
)

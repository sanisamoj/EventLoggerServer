package com.sanisamoj.data.models.dataclass

data class CreateGroupRequest(
    val title: String,
    val description: String,
    val imgProfileUrl: String,
    val superAdmins: List<String>
)

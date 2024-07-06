package com.sanisamoj.data.models.responses

import com.sanisamoj.utils.pagination.PaginationResponse
import kotlinx.serialization.Serializable

@Serializable
data class LogEventWithPaginationResponse(
    val content: List<LogEventResponse>,
    val paginationResponse: PaginationResponse
)
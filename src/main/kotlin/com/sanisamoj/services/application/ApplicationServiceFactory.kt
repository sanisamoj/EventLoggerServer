package com.sanisamoj.services.application

import com.sanisamoj.data.models.generics.ApplicationServiceData
import com.sanisamoj.data.models.responses.ApplicationServiceResponse

object ApplicationServiceFactory {
    fun applicationServiceResponse(applicationService: ApplicationServiceData): ApplicationServiceResponse {
        return ApplicationServiceResponse(
            id = applicationService.id.toString(),
            applicationName = applicationService.applicationName,
            description = applicationService.description,
            createdAt = applicationService.createdAt.toString()
        )
    }
}
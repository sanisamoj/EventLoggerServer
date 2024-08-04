package com.sanisamoj.services.application

import com.sanisamoj.context.GlobalContext
import com.sanisamoj.context.GlobalContext.APPLICATION_SERVICE_TOKEN_EXPIRATION
import com.sanisamoj.data.models.dataclass.*
import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.database.mongodb.Fields
import com.sanisamoj.database.mongodb.OperationField
import com.sanisamoj.utils.generators.TokenGenerator
import org.bson.types.ObjectId
import org.mindrot.jbcrypt.BCrypt

class ApplicationService(private val databaseRepository: DatabaseRepository = GlobalContext.databaseRepository) {
    suspend fun create(createApplicationServiceRequest: CreateApplicationServiceRequest): ApplicationServiceResponse {
        val applicationName = createApplicationServiceRequest.applicationName
        val description = createApplicationServiceRequest.description
        val hashedPassword = BCrypt.hashpw(createApplicationServiceRequest.password , BCrypt.gensalt())

        val applicationServiceData = databaseRepository.createApplicationService(applicationName, description, hashedPassword)
        return ApplicationServiceFactory.applicationServiceResponse(applicationServiceData)
    }

    suspend fun login(login: ApplicationServiceLoginRequest): AuthenticationResponse {
        val applicationService = databaseRepository.getApplicationServiceByName(login.applicationName)
        val isPasswordCorrect = BCrypt.checkpw(login.password, applicationService.password)
        if (!isPasswordCorrect) throw Exception(Errors.InvalidEmailOrPassword.description)

        val tokenInfo = TokenInfo(
            id = applicationService.id.toString(),
            email = applicationService.applicationName,
            sessionId = ObjectId().toString(),
            time = APPLICATION_SERVICE_TOKEN_EXPIRATION
        )

        val token = TokenGenerator.applicationService(tokenInfo)

        return AuthenticationResponse(token)
    }

    suspend fun getAllApplicationService(): List<ApplicationServiceResponse> {
        val applicationServiceList = databaseRepository.getAllApplicationServices()
        val applicationServiceResponseList: MutableList<ApplicationServiceResponse> = mutableListOf()
        applicationServiceList.forEach {
            applicationServiceResponseList.add(ApplicationServiceFactory.applicationServiceResponse(it))
        }
        return applicationServiceResponseList
    }

    suspend fun deleteApplicationServiceById(applicationId: String) {
        databaseRepository.deleteApplicationServiceById(applicationId)
    }

    suspend fun updatePassword(applicationId: String, newPassword: String) {
        val hashedPassword = BCrypt.hashpw(newPassword , BCrypt.gensalt())
        val update = OperationField(Fields.Password, hashedPassword)
        databaseRepository.updateApplicationService(applicationId, update)
    }

    fun updateMinVersion(isMobile: Boolean = false, newVersion: String) {
        GlobalContext.setMinVersion(isMobile, newVersion)
    }

    fun updateTargetVersion(isMobile: Boolean = false, newVersion: String) {
        GlobalContext.setTargetVersion(isMobile, newVersion)
    }
}
package com.sanisamoj.services.application

import com.sanisamoj.config.GlobalContext
import com.sanisamoj.config.GlobalContext.APPLICATION_SERVICE_TOKEN_EXPIRATION
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
        val applicationName: String = createApplicationServiceRequest.applicationName
        val description: String = createApplicationServiceRequest.description
        val hashedPassword: String = BCrypt.hashpw(createApplicationServiceRequest.password , BCrypt.gensalt())

        val applicationServiceData: ApplicationServiceData = databaseRepository.createApplicationService(applicationName, description, hashedPassword)
        return ApplicationServiceFactory.applicationServiceResponse(applicationServiceData)
    }

    suspend fun login(login: ApplicationServiceLoginRequest): AuthenticationResponse {
        val applicationService: ApplicationServiceData = databaseRepository.getApplicationServiceByName(login.applicationName)
        val isPasswordCorrect: Boolean = BCrypt.checkpw(login.password, applicationService.password)
        if (!isPasswordCorrect) throw Exception(Errors.InvalidEmailOrPassword.description)

        val tokenInfo = TokenInfo(
            id = applicationService.id.toString(),
            email = applicationService.applicationName,
            sessionId = ObjectId().toString(),
            time = APPLICATION_SERVICE_TOKEN_EXPIRATION
        )

        val token: String = TokenGenerator.applicationService(tokenInfo)

        return AuthenticationResponse(token)
    }

    suspend fun getAllApplicationService(): List<ApplicationServiceResponse> {
        val applicationServiceList: List<ApplicationServiceData> = databaseRepository.getAllApplicationServices()
        val applicationServiceResponseList: MutableList<ApplicationServiceResponse> = mutableListOf()
        applicationServiceList.forEach {
            applicationServiceResponseList.add(ApplicationServiceFactory.applicationServiceResponse(it))
        }
        return applicationServiceResponseList
    }

    suspend fun deleteApplicationServiceById(applicationId: String) {
        databaseRepository.deleteApplicationServiceById(applicationId)
    }

    suspend fun updatePassword(updatedPasswordData: UpdatePasswordData) {
        val hashedPassword: String = BCrypt.hashpw(updatedPasswordData.password , BCrypt.gensalt())
        val update = OperationField(Fields.Password, hashedPassword)
        databaseRepository.updateApplicationService(updatedPasswordData.applicationId, update)
    }
}
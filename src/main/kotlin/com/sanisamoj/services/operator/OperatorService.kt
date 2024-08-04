package com.sanisamoj.services.operator

import com.sanisamoj.config.GlobalContext
import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.dataclass.CreateOperatorRequest
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.dataclass.OperatorResponse
import com.sanisamoj.database.mongodb.Fields
import com.sanisamoj.database.mongodb.OperationField

class OperatorService(private val databaseRepository: DatabaseRepository = GlobalContext.databaseRepository) {
    suspend fun create(createOperatorRequest: CreateOperatorRequest): OperatorResponse {
        verifyIfOperatorAlreadyExists(createOperatorRequest)
        verifyCreateOperatorRequest(createOperatorRequest)

        val operator: Operator = OperatorFactory.Operator(createOperatorRequest)
        val operatorInDb: Operator = databaseRepository.createOperator(operator)
        return OperatorFactory.operatorResponse(operatorInDb)
    }

    private suspend fun verifyIfOperatorAlreadyExists(createOperatorRequest: CreateOperatorRequest) {
        try {
            databaseRepository.getOperatorByEmail(createOperatorRequest.email)
        } catch (_: Throwable) {
            return
        }

        throw Exception(Errors.AccountAlreadyExists.description)
    }

    private fun verifyCreateOperatorRequest(createOperatorRequest: CreateOperatorRequest) {
        val validations: Map<String, String> = mapOf(
            "Name" to createOperatorRequest.name,
            "Email" to createOperatorRequest.email,
            "Phone" to createOperatorRequest.phone,
            "Password" to createOperatorRequest.password
        )

        validations.forEach { (_, value) ->
            if (value.isEmpty()) {
                throw IllegalArgumentException(Errors.DataIsMissing.description)
            }
        }
    }

    suspend fun delete(operatorId: String) {
        databaseRepository.deleteOperator(operatorId)
    }

    suspend fun blockOperatorByEmail(email: String) {
        val operator: Operator = databaseRepository.getOperatorByEmail(email)
        val update = OperationField(Fields.Status, OperatorStatus.Blocked.name)
        databaseRepository.updateOperator(operator.id.toString(), update)
    }
}
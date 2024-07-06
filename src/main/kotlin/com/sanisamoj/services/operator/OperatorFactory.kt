package com.sanisamoj.services.operator

import com.sanisamoj.data.models.generics.Operator
import com.sanisamoj.data.models.requests.CreateOperatorRequest
import com.sanisamoj.data.models.responses.OperatorResponse
import org.mindrot.jbcrypt.BCrypt

object OperatorFactory {
    fun operatorResponse(operator: Operator): OperatorResponse {
        return OperatorResponse(
            id = operator.id.toString(),
            name = operator.name,
            email = operator.email,
            phone = operator.phone,
            profileImage = operator.profileImage,
            createdAt = operator.createdAt.toString()
        )
    }

    fun Operator(createOperatorRequest: CreateOperatorRequest): Operator {
        val hashedPassword = BCrypt.hashpw(createOperatorRequest.password, BCrypt.gensalt())

        val operator = Operator(
            name = createOperatorRequest.name,
            email = createOperatorRequest.email,
            phone = createOperatorRequest.phone,
            password = hashedPassword
        )

        return operator
    }
}
package com.sanisamoj.errors

import com.sanisamoj.context.GlobalContext.MAX_FILE_SIZE
import com.sanisamoj.context.GlobalContext.actionMessages
import com.sanisamoj.context.GlobalContext.errorMessages
import com.sanisamoj.data.models.responses.ErrorResponse
import io.ktor.http.*

fun errorResponse(errorMessage: String?): Pair<HttpStatusCode, ErrorResponse> {
    println(errorMessage)

    val response = when (errorMessage) {

        errorMessages.dataIsMissing -> {
            HttpStatusCode.BadRequest to ErrorResponse(errorMessages.dataIsMissing)
        }

        errorMessages.invalidCode -> {
            HttpStatusCode.BadRequest to ErrorResponse(errorMessages.invalidCode)
        }

        errorMessages.expiredCode -> {
            HttpStatusCode.Forbidden to ErrorResponse(
                error = errorMessages.expiredCode,
                details = actionMessages.resendValidationCode
            )
        }

        errorMessages.accountAlreadyExists -> {
            HttpStatusCode.Conflict to ErrorResponse(errorMessages.accountAlreadyExists)
        }

        errorMessages.accountNotExists -> {
            HttpStatusCode.NotFound to ErrorResponse(errorMessages.accountNotExists)
        }

        errorMessages.inactiveAccount -> {
            HttpStatusCode.Forbidden to ErrorResponse(errorMessages.inactiveAccount, actionMessages.activateAccount)
        }

        errorMessages.blockedAccount -> {
            HttpStatusCode.Forbidden to ErrorResponse(errorMessages.blockedAccount, actionMessages.contactSupport)
        }

        errorMessages.accountNotActivated -> {
            HttpStatusCode.Forbidden to ErrorResponse(errorMessages.accountNotActivated, actionMessages.activateAccount)
        }

        errorMessages.invalidEmailOrPassword -> {
            HttpStatusCode.Unauthorized to ErrorResponse(
                errorMessages.invalidEmailOrPassword,
                actionMessages.verifyCredentials
            )
        }

        errorMessages.revokedToken -> {
            HttpStatusCode.Unauthorized to ErrorResponse(errorMessages.revokedToken, actionMessages.pleaseLoginAgain)
        }

        errorMessages.redisNotResponding -> {
            HttpStatusCode.ServiceUnavailable to ErrorResponse(
                errorMessages.serviceUnavailable,
                actionMessages.tryAgainLater
            )
        }

        errorMessages.botNotResponding -> {
            HttpStatusCode.ServiceUnavailable to ErrorResponse(
                errorMessages.serviceUnavailable,
                actionMessages.tryAgainLater
            )
        }

        errorMessages.imageTotalSizeUploadExceeded -> {
            HttpStatusCode.PayloadTooLarge to ErrorResponse(
                errorMessages.imageTotalSizeUploadExceeded,
                "${MAX_FILE_SIZE}mb limit exceeded!"
            )
        }

        errorMessages.imageHasNotBeenDeleted -> {
            HttpStatusCode.Conflict to ErrorResponse(errorMessages.imageHasNotBeenDeleted)
        }

        errorMessages.userAlreadyActivated -> {
            HttpStatusCode.Forbidden to ErrorResponse(errorMessages.invalidAlgorithmToken)
        }

        errorMessages.applicationNotExists -> {
            HttpStatusCode.NotFound to ErrorResponse(errorMessages.applicationNotExists)
        }

        else -> {
            HttpStatusCode.InternalServerError to ErrorResponse(errorMessages.unableToComplete)
        }
    }

    return response
}
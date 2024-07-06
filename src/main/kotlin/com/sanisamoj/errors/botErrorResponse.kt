package com.sanisamoj.errors

import com.sanisamoj.data.models.enums.BotErrors
import com.sanisamoj.data.models.enums.Errors
import com.sanisamoj.data.models.responses.ErrorBotResponse

suspend fun botErrorResponse(errorBotResponse: ErrorBotResponse) {
    println(errorBotResponse)
        when(errorBotResponse.error) {
            BotErrors.InvalidToken.description -> {
                throw Exception(Errors.ServiceUnavailable.description)
            }

            BotErrors.JwtMustBeProvided.description -> {
                throw Exception(BotErrors.JwtMustBeProvided.description)
            }

            BotErrors.UnableToCreateBot.description -> {
                throw Exception(Errors.ServiceUnavailable.description)
            }

            BotErrors.InvalidUsernameOrPassword.description -> {}

            else -> {}
        }
}
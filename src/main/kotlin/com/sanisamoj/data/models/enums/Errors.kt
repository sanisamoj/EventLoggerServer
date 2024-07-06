package com.sanisamoj.data.models.enums

import com.sanisamoj.context.GlobalContext.errorMessages

enum class Errors(val description: String) {
    AccountAlreadyExists(description = errorMessages.accountAlreadyExists),
    AccountNotExists(description = errorMessages.accountNotExists),
    InvalidEmailOrPassword(description = errorMessages.invalidEmailOrPassword),
    InvalidCode(description = errorMessages.invalidCode),
    ExpiredCode(description = errorMessages.expiredCode),
    AccountNotActivated(description = errorMessages.accountNotActivated),
    BlockedAccount(description = errorMessages.blockedAccount),
    RedisNotResponding(description = errorMessages.redisNotResponding),
    BotNotResponding(description = errorMessages.botNotResponding),
    ServiceUnavailable(description = errorMessages.serviceUnavailable),
    RevokedToken(description = errorMessages.revokedToken),
    BotDoesNotExists(description = errorMessages.botDoesNotExists),
    UnsupportedMediaType(description = errorMessages.unsupportedMediaType),
    UserAlreadyActivated(description = errorMessages.userAlreadyActivated),
    TooManyRequests(description = errorMessages.tooManyRequests),
    UnableToComplete(description = errorMessages.unableToComplete),
    ApplicationNotExists(description = errorMessages.applicationNotExists),
    LogEventNotExists(description = errorMessages.logEventNotExists),
    DataIsMissing(description = errorMessages.dataIsMissing),
    UnknownError(description = errorMessages.unknown)
}
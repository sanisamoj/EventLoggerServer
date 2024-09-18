package com.sanisamoj.data.models.enums

import com.sanisamoj.config.GlobalContext.errorMessages

enum class Errors(val description: String) {
    AccountAlreadyExists(description = errorMessages.accountAlreadyExists),
    AccountNotExists(description = errorMessages.accountNotExists),
    InvalidEmailOrPassword(description = errorMessages.invalidEmailOrPassword),
    InvalidCode(description = errorMessages.invalidCode),
    ExpiredCode(description = errorMessages.expiredCode),
    AccountNotActivated(description = errorMessages.accountNotActivated),
    BlockedAccount(description = errorMessages.blockedAccount),
    BotTokenNotUpdated(description = "Bot token not updated"),
    RedisNotResponding(description = errorMessages.redisNotResponding),
    RevokedToken(description = errorMessages.revokedToken),
    TooManyRequests(description = errorMessages.tooManyRequests),
    ApplicationNotExists(description = errorMessages.applicationNotExists),
    LogEventNotExists(description = errorMessages.logEventNotExists),
    DataIsMissing(description = errorMessages.dataIsMissing)
}
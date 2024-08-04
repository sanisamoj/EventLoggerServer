package com.sanisamoj.data.models.enums

enum class BotErrors(val description: String) {
    UnableToCreateBot("Unable to create bot!"),
    UnableToDeleteBot("Unable to delete bot!"),
    InternalServerError("Internal server error!"),
    UserNotFound("User not found!"),
    InvalidUsernameOrPassword("Invalid username/password!"),
    InvalidToken("Invalid token!"),
    BotServiceUnavailable("Bot service unavailable!")
}
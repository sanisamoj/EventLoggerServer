package com.sanisamoj.data.models.enums

enum class BotErrors(val description: String) {
    MaxParticipantsReached("Total number of participants reached!"),
    UserNotAdded("User not added in group!"),
    UserNotRemoved("User was not removed from the group!"),
    NoGroupsWereDeletes("No groups were deleted!"),
    UnableToCreateBot("Unable to create bot!"),
    UnableToDeleteBot("Unable to delete bot!"),
    InternalServerError("Internal server error!"),
    UserNotFound("User not found!"),
    InvalidUsernameOrPassword("Invalid username/password!"),
    CouldNotDeleteItem("Could not delete item!"),
    InvalidToken("Invalid token!"),
    BotServiceUnavailable("Bot service unavailable!"),
    JwtMustBeProvided("jwt must be provided"),
}
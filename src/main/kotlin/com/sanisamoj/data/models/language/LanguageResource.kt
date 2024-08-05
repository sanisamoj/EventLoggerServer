package com.sanisamoj.data.models.language

import kotlinx.serialization.Serializable

@Serializable
data class LanguageResource(
    val errorMessages: ErrorMessages = ErrorMessages(),
    val actionMessages: ActionMessages = ActionMessages()
)

@Serializable
data class ErrorMessages(
    val accountAlreadyExists: String = "Account already exists!",
    val invalidEmailOrPassword: String = "Invalid E-mail/password!",
    val accountNotActivated: String = "Account not activated!",
    val invalidDocument: String = "Invalid document!",
    val maximumCharactersExceeded: String = "Maximum characters exceeded!",
    val internalServerError: String = "Internal server error!",
    val blockedAccount: String = "Blocked account!",
    val accountNotExists : String = "Account does not exist!",
    val expiredCode: String = "Expired code!",
    val unableToComplete: String = "Unable to complete this action!",
    val invalidCode: String = "Invalid code!",
    val inactiveAccount: String = "Inactive account!",
    val revokedToken: String = "Revoked token!",
    val redisNotResponding: String = "Redis is not responding!",
    val serviceUnavailable: String = "Service unavailable!",
    val botNotResponding: String = "Bot is not responding!",
    val drawNotExists: String = "Draw does not exist!",
    val drawContainsPurchased: String = "Draw contains purchased!",
    val botUnableToCreateGroup: String = "Bot is unable to create group!",
    val botUnableToDeleteGroup: String = "Bot is unable to delete group!",
    val botUnableToSendMessageToGroup: String = "Bot is unable to send message!",
    val botUnableToAddParticipant: String = "Bot is unable to add participant to the group!",
    val botUnableToRemoveParticipant: String = "Bot is unable to remove participant to the group!",
    val groupDoNotExists: String = "Group does not exist!",
    val botDoesNotExists: String = "Bot does not exist!",
    val errorCreatingPaymentByPix: String = "Error when creating a payment with pix!",
    val errorCreatingPaymentByCredit: String = "Error when creating a payment with credit!",
    val drawNotFound: String = "Draw not found!",
    val commentNotFound: String = "Comment not found!",
    val divergentOwner: String = "Divergent Owner!",
    val dataInCacheNotFound: String = "Data in cache mot found!",
    val orderNotFound: String = "Order not found!",
    val redemptionCodeNotFound: String = "Redemption code not found!",
    val expiredRedemptionCode: String = "Expired redemption code!",
    val maximumUsageReached: String = "Maximum usage reached!",
    val alreadyBeenRedeemed: String = "The code has already been redeemed!",
    val noItemsWereAdded: String = "No items were added!",
    val shoppingCartIsEmpty: String = "Shopping cart is empty!",
    val thereRafflesHaveAlreadyPurchased: String = "There are raffles that have already been purchased!",
    val codeCannotBeRedeemedByUser: String = "This code cannot be redeemed by the user!",
    val minimumValueHasNotReached: String = "Minimum value has not been reached!",
    val paymentServiceUnavailable: String = "Payment service unavailable!",
    val invalidPaymentMethod: String = "Invalid payment method!",
    val unsupportedMediaType: String = "Unsupported media type!",
    val imageTotalSizeUploadExceeded: String = "Image total size upload exceeded!",
    val imageHasNotBeenDeleted: String = "The image has not been deleted!",
    val imageNotExists: String = "Image does not exist!",
    val paymentNotAuthorized: String = "Payment not authorized!",
    val paymentDeclined: String = "Payment declined!",
    val unknown: String = "Unknown error!",
    val incorrectData: String = "Some data is incorrect!",
    val unableToAuthenticatePaymentService: String = "Unable to authenticate payment!",
    val invalidAlgorithmToken: String = "The provided Algorithm doesn't match the one defined in the JWT's Header.",
    val userAlreadyActivated: String = "User already been activated!",
    val raffleNotPurchased: String = "Raffles not purchased!",
    val accountNotFoundInBlockedAccounts: String = "Account not found in blocked accounts!",
    val invalidAccountType: String = "Invalid account type!",
    val tooManyRequests: String = "Too many requests!",
    val applicationNotExists: String = "Application does not exist!",
    val logEventNotExists: String = "Log does not exist!",
    val dataIsMissing: String = "Some data is missing!"
)

@Serializable
data class ActionMessages(
    val activateYourAccount: String = "Activate your account.",
    val resendValidationCode: String = "Resend validation code!",
    val activateAccount: String = "Please activate your account!",
    val contactSupport: String = "Contact support.",
    val pleaseLoginAgain: String = "Please login again!",
    val tryAgainLater: String = "Try again later!",
    val removePurchased: String = "Please remove purchased tickets."
)



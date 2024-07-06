package com.sanisamoj.data.models.language

import kotlinx.serialization.Serializable

@Serializable
data class LanguageResource(
    val errorMessages: ErrorMessages = ErrorMessages(),
    val actionMessages: ActionMessages = ActionMessages(),
    val infoMessages: InfoMessages = InfoMessages(),
    val testMessages: TestMessages = TestMessages(),
    val warningMessages: WarningMessages = WarningMessages()
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
    val verifyCredentials: String = "Please check if you entered your email or password correctly",
    val pleaseLoginAgain: String = "Please login again!",
    val tryAgainLater: String = "Try again later!",
    val removePurchased: String = "Please remove purchased tickets."
)

@Serializable
data class InfoMessages(
    val accountCreated: String = "Account created.",
    val validationCode: String = "This is the validation code, return to the app and enter the code. Do not share this code with third parties, and this code will only be available for 5 minutes",
    val drawOpen: String = "Open for shopping!",
    val drawClosed: String = "Closed for shopping!",
    val drawFinished: String = "Finished!",
    val waitingForTheResult: String = "Waiting for the result!",
    val itemsHaveNotBeenAddedOrRemoved: String = "Items have not been added or removed.",
    val onlyMediaTypeAllowed: String = "Only media types are allowed: [ .jpeg, .png, .jpg, .gif ]"
)

@Serializable
data class TestMessages(
    val creationAccountTest: String = "Creation account test.",
    val creationAccountWithTheSameEmailTest: String = "Creation account with the same email test.",
    val creationAccountWithWrongData: String = "Creation account with wrong data test.",
    val accountLoginTest: String = "Account login test",
    val accountLoginWithWrongPasswordTest: String = "Account login with wrong password test.",
    val accountActivateTest: String = "Account activation test",
    val accountActivateWithWrongCodeTest: String = "Account activation with wrong code test",
    val generateCodeValidationTest: String = "Account generate code validation test.",
    val generateCodeValidationWithNonexistentTest: String = "Account generate code validation with nonexistent email test.",
    val userAccountSessionTest: String = "User account session test.",
    val userAccountSessionWithWrongTokenTest: String = "User account session with wrong token test.",
    val userAccountSignoutTest: String = "User account sign out test.",
    val userAccountLoginWithRevokedTokenTest: String = "User account login with revoked token test.",
    val drawCreationTest: String = "Draw creation test.",
    val drawCreationWithModifiedTokenTest: String = "Draw creation with modified token test.",
    val drawUpdateRequestTest: String = "Draw update request test.",
    val drawDataComparisonTest: String = "Draw data comparison test.",
    val drawDeleteTest: String = "Draw delete test.",
    val deleteNoneDrawTest: String = "Delete nonexistent draw test.",
    val deleteDrawWithModifiedToken: String = "Delete draw with modified token test.",
    val getDrawByIdForTheAdminTest: String = "Get draw by ID with the admin test.",
    val getDrawByIdTest: String = "Get draw by ID test.",
    val drawDataComparisonWithGenericResponseTest: String = "Draw data comparison with generic response test.",
    val paginationItemsCountTest: String = "Pagination items count test.",
    val createModeratorCommentTest: String = "Moderator comment creation test.",
    val createUserCommentTest: String = "User comment creation test.",
    val deleteCommentByAdminTest: String = "Delete comment by admin test.",
    val checkEligibilityRedemptionCodeTest: String = "Check redemption code eligibility test.",
    val checkExpiredCodeTest: String = "Check expired code test.",
    val createRedemptionCodeTest: String = "Create redemption code test.",
    val deleteRedemptionCodeTest: String = "Delete redemption code test",
    val dataComparisonTest: String = "Data comparison test.",
    val addItemToShoppingCart: String = "Addition item to shopping cart test.",
    val removeItemToShoppingCart: String = "Remove item to shopping cart test.",
    val creatingOrderWithoutItems: String = "Creating order without items in cart test.",
    val creatingOrderWithPix: String = "Creating order with pix test.",
    val creatingOrderWithCredit: String = "Creating order with credit test.",
    val createCryptographyCard: String = "Create cryptography card test.",
    val purchasedDataComparison: String = "Purchased data comparison test.",
    val getImage: String = "Get image test.",
    val postImageByAdmin: String = "Post image by admin test.",
    val deleteImageByAdmin: String = "Delete image by admin test.",
    val verifyIfFileExists: String = "Verify if file does exist test.",
    val setImageProfile: String = "Set image profile test.",
    val removeImageProfile: String = "Remove image profile test.",
    val dataComparisonImageProfile: String = "Data comparison image profile test.",
)

@Serializable
data class WarningMessages(
    val yourVerificationCodeIs: String = "Your verification code is:",
    val doNotShareThisCode: String = "Do not share this code with anyone. It's only 5 minutes long"
)
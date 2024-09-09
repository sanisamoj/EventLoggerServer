package com.sanisamoj.routing

import com.sanisamoj.data.models.dataclass.CreateOperatorRequest
import com.sanisamoj.data.models.dataclass.OperatorLoginRequest
import com.sanisamoj.data.models.dataclass.ValidationCode
import com.sanisamoj.data.pages.confirmationPage
import com.sanisamoj.data.pages.tokenExpiredPage
import com.sanisamoj.services.operator.OperatorAuthenticationService
import com.sanisamoj.services.operator.OperatorService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.html
import kotlinx.html.stream.appendHTML

fun Route.operatorRouting() {
    route("/operator") {

        rateLimit(RateLimitName("register")) {

            // Route responsible for creating an operator
            post {
                val createOperatorRequest = call.receive<CreateOperatorRequest>()
                val operatorResponse = OperatorService().create(createOperatorRequest)
                return@post call.respond(HttpStatusCode.Created, operatorResponse)
            }
        }

        rateLimit(RateLimitName("lightweight")) {

            // Responsible for deleting an operator
            delete {
                val operatorId = call.request.queryParameters["id"].toString()
                OperatorService().delete(operatorId)
                return@delete call.respond(HttpStatusCode.OK)
            }

            // Responsible for a verification token
            post("/generate") {
                val identification = call.request.queryParameters["operator"].toString()
                val mobile: Boolean = call.request.queryParameters["mobile"].toString() == "true"
                if(mobile) {
                    OperatorAuthenticationService().generateValidationCodeByWhatsapp(identification)
                } else {
                    OperatorAuthenticationService().generateValidationEmailToken(identification)
                }
                return@post call.respond(HttpStatusCode.OK)
            }

            // Responsible for validating a validation code to activate an account
            get("/activate") {
                val token = call.parameters["token"].toString()

                try {
                    OperatorAuthenticationService().activateOperatorByToken(token)
                    call.respondText(buildString {
                        appendHTML().html { confirmationPage() }
                    }, ContentType.Text.Html)

                } catch (_: Throwable) {
                    call.respondText(buildString {
                        appendHTML().html { tokenExpiredPage() }
                    }, ContentType.Text.Html)
                }
            }

            // Responsible for validation a validation code to activate an account by whatsapp
            post("/activate") {
                val validationCode: ValidationCode = call.receive<ValidationCode>()
                OperatorAuthenticationService().activateOperatorByValidationCode(validationCode)
                return@post call.respond(HttpStatusCode.OK)
            }

            authenticate("operator-jwt") {

                // Responsible for carrying out the operator session
                post("/session") {
                    val principal = call.principal<JWTPrincipal>()!!
                    val operatorId = principal.payload.getClaim("id").asString()

                    val operatorLoginResponse = OperatorAuthenticationService().session(operatorId)
                    return@post call.respond(operatorLoginResponse)
                }

                // Responsible for carrying out operator signout
                delete("/session") {
                    val principal = call.principal<JWTPrincipal>()!!
                    val session = principal.payload.getClaim("session").asString()

                    val operatorLoginResponse = OperatorAuthenticationService().signOut(session)
                    return@delete call.respond(operatorLoginResponse)
                }
            }

        }

        rateLimit(RateLimitName("login")) {

            // Responsible for logging in
            post("/login") {
                val operatorLoginRequest = call.receive<OperatorLoginRequest>()
                val operatorLoginResponse = OperatorAuthenticationService().login(operatorLoginRequest)
                return@post call.respond(operatorLoginResponse)
            }
        }
    }
}
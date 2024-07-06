package com.sanisamoj.controllers

import com.sanisamoj.data.models.requests.CreateOperatorRequest
import com.sanisamoj.data.models.requests.OperatorLoginRequest
import com.sanisamoj.data.models.requests.ValidationCodeRequest
import com.sanisamoj.data.pages.confirmationPage
import com.sanisamoj.data.pages.tokenExpiredPage
import com.sanisamoj.errors.errorResponse
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
            // Rota responsável por criar um operador
            post {
                val createOperatorRequest = call.receive<CreateOperatorRequest>()

                try {
                    val operatorResponse = OperatorService().create(createOperatorRequest)
                    return@post call.respond(HttpStatusCode.Created, operatorResponse)

                } catch (e: Throwable) {
                    val response = errorResponse(e.message!!)
                    return@post call.respond(response.first, message = response.second)
                }
            }
        }

        rateLimit(RateLimitName("lightweight")) {

            // Responsável por deletar um operador
            delete {
                val operadorId = call.request.queryParameters["id"].toString()

                try {
                    OperatorService().delete(operadorId)
                    return@delete call.respond(HttpStatusCode.OK)

                } catch (e: Throwable) {
                    val response = errorResponse(e.message!!)
                    return@delete call.respond(response.first, message = response.second)
                }
            }

            // Responsável um token de verificação
            post("/generate") {
                val identification = call.request.queryParameters["operator"].toString()

                try {
                    OperatorAuthenticationService().generateValidationEmailToken(identification)
                    return@post call.respond(HttpStatusCode.OK)

                } catch (e: Throwable) {
                    val response = errorResponse(e.message!!)
                    return@post call.respond(response.first, message = response.second)
                }
            }

            // Responsável por validar um código de validação para ativar uma conta
            get("/activate") {
                val token = call.parameters["token"].toString()

                try {
                    OperatorAuthenticationService().activateOperatorByToken(token)
                    call.respondText(buildString {
                        appendHTML().html { confirmationPage() }
                    }, ContentType.Text.Html)

                } catch (e: Throwable) {
                    call.respondText(buildString {
                        appendHTML().html { tokenExpiredPage() }
                    }, ContentType.Text.Html)
                }
            }

            authenticate("operator-jwt") {

                // Responsável por realizar a sessão do operador
                post("/session") {
                    val principal = call.principal<JWTPrincipal>()!!
                    val operatorId = principal.payload.getClaim("id").asString()
                    val session = principal.payload.getClaim("session").asString()

                    try {
                        val operatorLoginResponse = OperatorAuthenticationService().session(operatorId, session)
                        return@post call.respond(operatorLoginResponse)

                    } catch (e: Throwable) {
                        val response = errorResponse(e.message!!)
                        return@post call.respond(response.first, message = response.second)
                    }
                }

                // Responsável por realizar o signout do operador
                delete("/session") {
                    val principal = call.principal<JWTPrincipal>()!!
                    val operatorId = principal.payload.getClaim("id").asString()
                    val session = principal.payload.getClaim("session").asString()

                    try {
                        val operatorLoginResponse = OperatorAuthenticationService().signOut(operatorId, session)
                        return@delete call.respond(operatorLoginResponse)

                    } catch (e: Throwable) {
                        val response = errorResponse(e.message!!)
                        return@delete call.respond(response.first, message = response.second)
                    }
                }
            }

        }

        rateLimit(RateLimitName("login")) {
            // Responsável por realizar o login
            post("/login") {
                val operatorLoginRequest = call.receive<OperatorLoginRequest>()

                try {
                    val operatorLoginResponse = OperatorAuthenticationService().login(operatorLoginRequest)
                    return@post call.respond(operatorLoginResponse)

                } catch (e: Throwable) {
                    val response = errorResponse(e.message!!)
                    return@post call.respond(response.first, message = response.second)
                }
            }
        }
    }
}
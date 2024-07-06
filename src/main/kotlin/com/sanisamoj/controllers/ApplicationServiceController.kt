package com.sanisamoj.controllers

import com.sanisamoj.context.GlobalContext
import com.sanisamoj.data.models.generics.VersionResponse
import com.sanisamoj.data.models.requests.ApplicationServiceLoginRequest
import com.sanisamoj.data.models.requests.CreateApplicationServiceRequest
import com.sanisamoj.errors.errorResponse
import com.sanisamoj.services.application.ApplicationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.applicationServiceRouting() {
    route("/application") {

        // Rota responsável por retornar a versão atual
        get("/version") {
            val mobile = call.parameters["mobile"]

            val version = VersionResponse(
                serverVersion = GlobalContext.serverVersion,
                frontMinVersion = if(mobile == null) GlobalContext.desktopMinVersion else GlobalContext.mobileMinVersion,
                frontTargetVersion = if(mobile == null) GlobalContext.desktopTargetVersion else GlobalContext.mobileTargetVersion
            )
            return@get call.respond(version)
        }

        // Responsável por realizar o login
        post("/login") {
            val applicationServiceLoginRequest = call.receive<ApplicationServiceLoginRequest>()

            try {
                val authenticationResponse = ApplicationService().login(applicationServiceLoginRequest)
                return@post call.respond(authenticationResponse)

            } catch (e: Throwable) {
                val response = errorResponse(e.message!!)
                return@post call.respond(response.first, message = response.second)
            }
        }

        authenticate("operator-jwt") {

            // Rota responsável por criar uma ApplicationService
            post{
                val createApplicationServiceRequest = call.receive<CreateApplicationServiceRequest>()

                try {
                    val applicationResponse = ApplicationService().create(createApplicationServiceRequest)
                    return@post call.respond(HttpStatusCode.Created, applicationResponse)

                } catch (e: Throwable) {
                    val response = errorResponse(e.message!!)
                    return@post call.respond(response.first, message = response.second)
                }
            }

            // Responsável por retornar todos as aplicações
            get {
                 try {
                     val applicationServiceResponseList = ApplicationService().getAllApplicationService()
                     return@get call.respond(applicationServiceResponseList)

                 } catch (e: Throwable) {
                     val response = errorResponse(e.message!!)
                     return@get call.respond(response.first, message = response.second)
                 }
            }

            // Responsável por deletar uma aplicação
            delete {
                val applicationId = call.parameters["id"].toString()

                try {
                    ApplicationService().deleteApplicationServiceById(applicationId)
                    return@delete call.respond(HttpStatusCode.OK)
                } catch (e: Throwable) {
                    val response = errorResponse(e.message!!)
                    return@delete call.respond(response.first, message = response.second)
                }
            }

        }
    }
}
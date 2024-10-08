package com.sanisamoj.routing

import com.sanisamoj.config.GlobalContext
import com.sanisamoj.data.models.dataclass.ApplicationServiceLoginRequest
import com.sanisamoj.data.models.dataclass.CreateApplicationServiceRequest
import com.sanisamoj.data.models.dataclass.UpdatePasswordData
import com.sanisamoj.data.models.dataclass.VersionResponse
import com.sanisamoj.services.application.ApplicationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.applicationServiceRouting() {
    route("/application") {

        // Route responsible for returning the current version
        get("/version") {
            val version = VersionResponse(GlobalContext.VERSION)
            return@get call.respond(version)
        }

        // Responsible for logging in
        post("/login") {
            val applicationServiceLoginRequest = call.receive<ApplicationServiceLoginRequest>()
            val authenticationResponse = ApplicationService().login(applicationServiceLoginRequest)
            return@post call.respond(authenticationResponse)
        }

        authenticate("operator-jwt") {

            // Route responsible for creating an ApplicationService
            post{
                val createApplicationServiceRequest = call.receive<CreateApplicationServiceRequest>()
                val applicationResponse = ApplicationService().create(createApplicationServiceRequest)
                return@post call.respond(HttpStatusCode.Created, applicationResponse)
            }

            // Responsible for returning all applications
            get {
                val applicationServiceResponseList = ApplicationService().getAllApplicationService()
                return@get call.respond(applicationServiceResponseList)
            }

            // Responsible for deleting an application
            delete {
                val applicationId = call.parameters["id"].toString()
                ApplicationService().deleteApplicationServiceById(applicationId)
                return@delete call.respond(HttpStatusCode.OK)
            }

            // Responsible for updating password
            post("/password") {
                val updatePasswordData = call.receive<UpdatePasswordData>()
                ApplicationService().updatePassword(updatePasswordData)
                return@post call.respond(HttpStatusCode.OK)
            }

        }
    }
}
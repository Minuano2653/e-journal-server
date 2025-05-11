package com.example.controllers

import com.example.models.dtos.CreateStudentRequest
import com.example.models.dtos.CreateTeacherRequest
import com.example.models.dtos.UserCreationResponse
import com.example.services.UserService
import com.example.utils.getRoleId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserController(private val userService: UserService) {

    suspend fun createStudent(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()

        val roleId = principal?.getRoleId()
        if (roleId == null) {
            call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
            return
        }

        try {
            val request = call.receive<CreateStudentRequest>()
            val userId = userService.createStudent(request, roleId)

            if (userId != null) {
                call.respond(UserCreationResponse(userId.toString()))
            } else {
                call.respondText(
                    "Forbidden: Only administrators can create students",
                    status = HttpStatusCode.Forbidden
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun createTeacher(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()

        val roleId = principal?.getRoleId()
        if (roleId == null) {
            call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
            return
        }

        try {
            val request = call.receive<CreateTeacherRequest>()
            val userId = userService.createTeacher(request, roleId)

            if (userId != null) {
                call.respond(UserCreationResponse(userId.toString()))
            } else {
                call.respondText(
                    "Forbidden: Only administrators can create teachers",
                    status = HttpStatusCode.Forbidden
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

}
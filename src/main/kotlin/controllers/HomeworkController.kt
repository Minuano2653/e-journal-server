package com.example.controllers

import com.example.models.dtos.CreateHomeworkRequest
import com.example.models.dtos.UpdateHomeworkRequest
import com.example.services.HomeworkService
import com.example.utils.getRoleId
import com.example.utils.getUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*


class HomeworkController(private val homeworkService: HomeworkService) {
    suspend fun createHomework(call: ApplicationCall) {
        try {
            val principal = call.principal<JWTPrincipal>()
            val teacherId = principal?.getUserId()
            val roleId = principal?.getRoleId()


            if (teacherId == null || roleId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
                return
            }

            val request = call.receive<CreateHomeworkRequest>()
            val homeworkId = homeworkService.createHomework(request, teacherId.toString(), roleId)

            if (homeworkId != null) {
                call.respond(HttpStatusCode.OK, mapOf("homeworkId" to homeworkId))
            } else {
                call.respond(
                    HttpStatusCode.Forbidden,
                    "You do not have permission to create homework for this assignment"
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }

    suspend fun updateHomework(call: ApplicationCall) {
        try {
            val principal = call.principal<JWTPrincipal>()
            val teacherId = principal?.getUserId()
            val roleId = principal?.getRoleId()

            if (teacherId == null || roleId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
                return
            }

            val request = call.receive<UpdateHomeworkRequest>()
            val success = homeworkService.updateHomework(request, roleId)

            if (success) {
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            } else {
                call.respond(
                    HttpStatusCode.Forbidden,
                    "You do not have permission to update this homework assignment"
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }

    suspend fun getHomeworkForTeacher(call: ApplicationCall) {
        try {
            val principal = call.principal<JWTPrincipal>()
            val teacherId = principal?.getUserId()
            val roleId = principal?.getRoleId()

            if (teacherId == null || roleId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
                return
            }

            val groupId = call.parameters["groupId"]
            if (groupId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing group ID")
                return
            }

            val subjectId = call.parameters["subjectId"]?.toInt()
            if (subjectId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing subject ID")
                return
            }

            val date = call.parameters["date"]
            if (date == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing date")
                return
            }
            val homework = homeworkService.getHomeworkForTeacher(groupId, subjectId, date, teacherId, roleId)
            if (homework != null) {
                call.respond(HttpStatusCode.OK, homework)
            } else {
                call.respond(HttpStatusCode.NotFound, "Homework not found")
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }
}
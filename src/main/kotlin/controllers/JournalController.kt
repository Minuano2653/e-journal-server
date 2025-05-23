package com.example.controllers

import com.example.models.dtos.StudentGradesResponse
import com.example.models.entities.Role
import com.example.services.JournalService
import com.example.utils.getRoleId
import com.example.utils.getUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.time.LocalDate
import java.util.*


class JournalController(private val journalService: JournalService) {

    suspend fun getStudentGrades(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()
        val studentId = principal?.getUserId()
        val roleId = principal?.getRoleId()

        if (studentId == null || roleId == null) {
            call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
            return
        }

        try {
            // Get parameters from the request
            val subjectId = call.parameters["subjectId"]?.toIntOrNull()
            val year = call.parameters["year"]?.toIntOrNull()
            val month = call.parameters["month"]?.toIntOrNull()

            // Validate required parameters
            if (subjectId == null || year == null || month == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Missing required parameters: subjectId, year, and month are required")
                )
                return
            }

            // Validate that users can only access their own grades unless they are a teacher or admin
            if (roleId != Role.RoleIds.STUDENT) {
                call.respond(
                    HttpStatusCode.Forbidden,
                    mapOf("error" to "Students can only access their own grades")
                )
                return
            }

            // Get grades from the service
            val grades = journalService.getStudentGradesForMonth(studentId, subjectId, year, month)

            call.respond(StudentGradesResponse(grades))

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun getStudentPerformance(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()
        val studentId = principal?.getUserId()
        val roleId = principal?.getRoleId()

        if (studentId == null || roleId == null) {
            call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
            return
        }

        try {
            // Get parameters from the request
            val subjectId = call.parameters["subjectId"]?.toIntOrNull()

            // Validate required parameters
            if (subjectId == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Missing required parameter: subjectId is required")
                )
                return
            }

            // Validate that users can only access their own performance unless they are a teacher or admin
            if (roleId != Role.RoleIds.STUDENT) {
                call.respond(
                    HttpStatusCode.Forbidden,
                    mapOf("error" to "Students can only access their own performance data")
                )
                return
            }

            // Get performance data from the service
            val performanceData = journalService.getStudentPerformance(studentId, subjectId)

            call.respond(HttpStatusCode.OK, performanceData)

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun getGroupGrades(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()
        val teacherId = principal?.getUserId()
        val roleId = principal?.getRoleId()

        if (teacherId == null || roleId == null) {
            call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
            return
        }

        try {
            // Get parameters from the request
            val groupId = call.parameters["groupId"]
            val subjectId = call.parameters["subjectId"]?.toIntOrNull()
            val date = call.parameters["date"]

            // Validate required parameters
            if (groupId == null || subjectId == null || date == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Missing required parameters: groupId, subjectId, and date are required")
                )
                return
            }

            // Validate that only teachers can access this endpoint
            if (roleId != Role.RoleIds.TEACHER) {
                call.respond(
                    HttpStatusCode.Forbidden,
                    mapOf("error" to "Only teachers can access group grades")
                )
                return
            }

            // Parse date
            val parsedDate = try {
                LocalDate.parse(date)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid date format. Use yyyy-MM-dd")
                )
                return
            }

            // Get group grades from the service
            val groupGrades = journalService.getGroupGradesForDate(
                teacherId,
                UUID.fromString(groupId),
                subjectId,
                parsedDate
            )

            if (groupGrades == null) {
                call.respond(
                    HttpStatusCode.Forbidden,
                    mapOf("error" to "You don't have permission to access this group's grades")
                )
                return
            }

            call.respond(HttpStatusCode.OK, groupGrades)

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }
}
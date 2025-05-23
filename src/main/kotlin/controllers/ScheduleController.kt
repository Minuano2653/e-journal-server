package com.example.controllers

import com.example.services.ScheduleService
import com.example.services.UserService
import com.example.utils.getUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.time.LocalDate


class ScheduleController(
    private val scheduleService: ScheduleService,
    private val userService: UserService
) {
    suspend fun getGroupScheduleForDay(call: ApplicationCall) {
        try {
            val principal = call.principal<JWTPrincipal>()

            val studentId = principal?.getUserId()
            if (studentId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
                return
            }

            val date = call.parameters["date"]
            if (date == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid date")
                return
            }

            val groupId = userService.findStudentGroupId(studentId)
            if (groupId == null) {
                call.respond(HttpStatusCode.NotFound, "Student is not assigned to a group")
                return
            }

            val schedule = scheduleService.getGroupScheduleWithHomeworkForDay(groupId, LocalDate.parse(date))
            call.respond(schedule)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }

    suspend fun getTeacherScheduleForDay(call: ApplicationCall) {
        try {
            val principal = call.principal<JWTPrincipal>()
            val teacherId = principal?.getUserId()

            if (teacherId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
                return
            }

            val date = call.parameters["date"]
            if (date == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid date")
                return
            }

            val schedule = scheduleService.getTeacherScheduleForDay(teacherId, LocalDate.parse(date))
            call.respond(schedule)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }
}
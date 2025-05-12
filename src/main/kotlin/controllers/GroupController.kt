package com.example.controllers

import com.example.models.dtos.AdministratorGroupsResponse
import com.example.models.dtos.CreateGroupRequest
import com.example.models.dtos.TeacherGroupsResponse
import com.example.services.GroupService
import com.example.utils.getRoleId
import com.example.utils.getUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class GroupController(private val groupService: GroupService) {
    suspend fun createGroup(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()

        val roleId = principal?.getRoleId()
        if (roleId == null) {
            call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
            return
        }

        val request = call.receive<CreateGroupRequest>()
        val groupId = groupService.createGroup(request, roleId)

        try {
            if (groupId != null) {
                call.respond(mapOf("groupId" to groupId.toString()))
            } else {
                call.respondText(
                    "Forbidden: Only administrators can create groups",
                    status = HttpStatusCode.Forbidden
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.Conflict, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    suspend fun getTeacherGroups(call: ApplicationCall) {
        try {
            val principal = call.principal<JWTPrincipal>()
            val teacherId = principal?.getUserId()

            if (teacherId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
                return
            }

            val groups = groupService.getTeacherGroups(teacherId)
            call.respond(TeacherGroupsResponse(groups))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }

    suspend fun getAllGroups(call: ApplicationCall) {
        try {
            val principal = call.principal<JWTPrincipal>()
            val usersId = principal?.getUserId()

            if (usersId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Missing or invalid JWT")
                return
            }

            val groups = groupService.getAllGroups()
            call.respond(AdministratorGroupsResponse(groups))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error"))
            )
        }
    }
}

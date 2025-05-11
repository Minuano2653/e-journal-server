package com.example.controllers

import com.example.models.dtos.CreateGroupRequest
import com.example.services.GroupService
import com.example.utils.getRoleId
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
}

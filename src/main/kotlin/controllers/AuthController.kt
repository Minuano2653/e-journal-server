package com.example.controllers

import com.example.models.dtos.LoginRequest
import com.example.services.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class AuthController(private val authService: AuthService) {
    suspend fun login(call: ApplicationCall) {
        val request = call.receive<LoginRequest>()
        val token = authService.login(request)
        if (token != null) {
            call.respond(mapOf("token" to token))
        } else {
            call.respondText("Invalid credentials", status = HttpStatusCode.Unauthorized)
        }
    }
}
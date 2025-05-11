package com.example.routes

import com.example.controllers.AuthController
import io.ktor.server.routing.*

fun Route.authRoutes(controller: AuthController) {
    route("/auth") {
        post("/login") {
            controller.login(call)
        }
    }
}
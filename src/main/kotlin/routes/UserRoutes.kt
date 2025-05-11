package com.example.routes

import com.example.controllers.UserController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.userRoutes(userController: UserController) {
    authenticate("auth-jwt") {
        route("/users") {
            post("/students") {
                userController.createStudent(call)
            }

            post("/teachers") {
                userController.createTeacher(call)
            }
        }
    }
}
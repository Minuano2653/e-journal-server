package com.example.routes

import com.example.controllers.GroupController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.groupRoutes(groupController: GroupController) {
    authenticate("auth-jwt") {
        post("/groups") {
            groupController.createGroup(call)
        }
    }
}
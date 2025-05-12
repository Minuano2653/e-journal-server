package com.example.routes

import com.example.controllers.GroupController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.groupRoutes(groupController: GroupController) {
    authenticate("auth-jwt") {
        route("/groups") {
            post {
                groupController.createGroup(call)
            }

            get("/teacher") {
                groupController.getTeacherGroups(call)
            }

            get("/admin") {
                groupController.getAllGroups(call)
            }
        }
    }
}
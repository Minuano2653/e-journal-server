package com.example.routes

import com.example.controllers.HomeworkController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.homeworkRoutes(homeworkController: HomeworkController) {
    authenticate("auth-jwt") {
        route("/homework") {
            post {
                homeworkController.createHomework(call)
            }

            put {
                homeworkController.updateHomework(call)
            }

            get("/group/{groupId}/subject/{subjectId}/date/{date}") {
                homeworkController.getHomeworkForTeacher(call)
            }
        }
    }
}
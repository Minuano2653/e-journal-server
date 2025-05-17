package com.example.routes

import com.example.controllers.JournalController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.journalRoutes(journalController: JournalController) {
    authenticate("auth-jwt") {
        route("/journal") {
            get("/grades/subject/{subjectId}/year/{year}/month/{month}") {
                journalController.getStudentGrades(call)
            }

            get("/performance/subject/{subjectId}") {
                journalController.getStudentPerformance(call)
            }
        }
    }
}
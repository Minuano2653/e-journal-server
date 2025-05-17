package com.example.routes

import com.example.controllers.JournalController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.journalRoutes(journalController: JournalController) {
    authenticate("auth-jwt") {
        route("/journal") {
            get("/grades/{subjectId}/{year}/{month}") {
                journalController.getStudentGrades(call)
            }


        }
    }
}
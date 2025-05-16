package com.example.routes

import com.example.controllers.ScheduleController
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.scheduleRoutes(scheduleController: ScheduleController) {
    authenticate("auth-jwt") {
        route("/schedule") {
            get("/group/date/{date}") {
                scheduleController.getGroupScheduleForDay(call)
            }

            get("/teacher/day/{dayOfWeek}") {
                scheduleController.getTeacherScheduleForDay(call)
            }
        }
    }
}
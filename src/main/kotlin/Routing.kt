package com.example

import com.example.controllers.*
import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val authController: AuthController by inject()
    val groupController: GroupController by inject()
    val userController: UserController by inject()
    val scheduleController: ScheduleController by inject()
    val homeworkController: HomeworkController by inject()
    val journalController: JournalController by inject()

    routing {
        authRoutes(authController)
        groupRoutes(groupController)
        userRoutes(userController)
        scheduleRoutes(scheduleController)
        homeworkRoutes(homeworkController)
        journalRoutes(journalController)
    }
}
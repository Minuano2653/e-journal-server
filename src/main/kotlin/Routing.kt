package com.example

import com.example.controllers.AuthController
import com.example.controllers.GroupController
import com.example.routes.authRoutes
import com.example.routes.groupRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val authController: AuthController by inject()
    val groupController: GroupController by inject()

    routing {
        authRoutes(authController)
        groupRoutes(groupController)
    }
}
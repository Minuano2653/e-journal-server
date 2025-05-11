package com.example

import com.example.controllers.AuthController
import com.example.controllers.GroupController
import com.example.controllers.UserController
import com.example.routes.authRoutes
import com.example.routes.groupRoutes
import com.example.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val authController: AuthController by inject()
    val groupController: GroupController by inject()
    val userController: UserController by inject()

    routing {
        authRoutes(authController)
        groupRoutes(groupController)
        userRoutes(userController)
    }
}
package com.example.di

import com.example.controllers.AuthController
import com.example.controllers.GroupController
import com.example.controllers.UserController
import com.example.repositories.GroupRepository
import com.example.repositories.GroupRepositoryImpl
import com.example.repositories.UserRepository
import com.example.repositories.UserRepositoryImpl
import com.example.services.AuthService
import com.example.services.GroupService
import com.example.services.UserService
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single<UserRepository> { UserRepositoryImpl() }
    single<GroupRepository> { GroupRepositoryImpl() }

    // Services
    single { AuthService(get()) }
    single { GroupService(get()) }
    single { UserService(get()) }

    // Контроллеры
    single { AuthController(get()) }
    single { GroupController(get()) }
    single { UserController(get()) }
}
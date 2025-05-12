package com.example.di

import com.example.controllers.AuthController
import com.example.controllers.GroupController
import com.example.controllers.ScheduleController
import com.example.controllers.UserController
import com.example.repositories.*
import com.example.services.AuthService
import com.example.services.GroupService
import com.example.services.ScheduleService
import com.example.services.UserService
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single<UserRepository> { UserRepositoryImpl() }
    single<GroupRepository> { GroupRepositoryImpl() }
    single<ScheduleRepository> { ScheduleRepositoryImpl() }

    // Services
    single { AuthService(get()) }
    single { GroupService(get()) }
    single { UserService(get()) }
    single { ScheduleService(get()) }

    // Контроллеры
    single { AuthController(get()) }
    single { GroupController(get()) }
    single { UserController(get()) }
    single { ScheduleController(get(), get()) }
}
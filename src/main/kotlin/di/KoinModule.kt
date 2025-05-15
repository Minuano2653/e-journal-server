package com.example.di

import com.example.controllers.*
import com.example.repositories.*
import com.example.services.*
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single<UserRepository> { UserRepositoryImpl() }
    single<GroupRepository> { GroupRepositoryImpl() }
    single<ScheduleRepository> { ScheduleRepositoryImpl() }
    single<HomeworkRepository> { HomeworkRepositoryImpl() }

    // Services
    single { AuthService(get()) }
    single { GroupService(get()) }
    single { UserService(get()) }
    single { ScheduleService(get()) }
    single { HomeworkService(get()) }

    // Контроллеры
    single { AuthController(get()) }
    single { GroupController(get()) }
    single { UserController(get()) }
    single { ScheduleController(get(), get()) }
    single { HomeworkController(get()) }
}
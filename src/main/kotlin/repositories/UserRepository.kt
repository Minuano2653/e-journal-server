package com.example.repositories

import com.example.models.dtos.UserDto

interface UserRepository {
    fun findByEmailAndPassword(email: String, password: String): UserDto?
}
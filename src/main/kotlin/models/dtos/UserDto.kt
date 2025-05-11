package com.example.models.dtos

import java.util.*

data class UserDto(
    val id: UUID,
    val email: String,
    val password: String,
    val role: Int
)

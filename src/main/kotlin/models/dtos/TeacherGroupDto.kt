package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TeacherGroupDto(
    val id: String,
    val name: String,
    val studentCount: Int
)
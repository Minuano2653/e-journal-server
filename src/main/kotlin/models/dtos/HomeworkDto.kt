package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class HomeworkDto(
    val id: Int,
    val assignmentId: Int,
    val subjectId: Int,
    val date: String, // ISO date format (yyyy-MM-dd)
    val description: String,
)
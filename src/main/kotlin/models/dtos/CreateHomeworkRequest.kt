package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CreateHomeworkRequest(
    val groupId: String,
    val subjectId: Int,
    val date: String, // ISO date format (yyyy-MM-dd)
    val description: String
)
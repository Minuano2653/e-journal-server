package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UpdateHomeworkRequest(
    val homeworkId: Int,
    val description: String
)
package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class GroupLessonDto(
    val lessonNumber: Int,
    val startTime: String,
    val endTime: String,
    val subject: String,
    val teacher: String,
    val classroom: String
)
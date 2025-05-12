package com.example.models.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherLessonDto(
    @SerialName("lesson_number")
    val lessonNumber: Int,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    val group: String,
    val subject: String,
    val classroom: String
)
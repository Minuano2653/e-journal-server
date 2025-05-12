package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TeacherScheduleResponse(
    val lessons: List<TeacherLessonDto>
)
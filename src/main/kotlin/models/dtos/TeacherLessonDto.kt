package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TeacherLessonDto(
    val lessonNumber: Int,
    val startTime: String,
    val endTime: String,
    val group: GroupDto,
    val subject: SubjectDto,
    val classroom: String
)


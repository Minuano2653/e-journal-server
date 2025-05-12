package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class GroupScheduleResponse(
    val lessons: List<GroupLessonDto>
)
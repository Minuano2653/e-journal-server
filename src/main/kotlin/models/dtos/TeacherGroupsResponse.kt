package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TeacherGroupsResponse(
    val groups: List<TeacherGroupDto>
)
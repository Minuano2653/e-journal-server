package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class GroupGradesResponse(
    val date: String,
    val groupName: String,
    val subjectName: String,
    val students: List<StudentGradeDto>
)
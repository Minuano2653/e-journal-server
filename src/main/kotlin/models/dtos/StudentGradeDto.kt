package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class StudentGradeDto(
    val studentId: String,
    val journalEntryId: Int? = null,
    val studentName: String,
    val grade: Int?,
    val attendanceStatus: Char?
)
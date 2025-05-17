package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class StudentPerformanceResponse(
    val quarterGrades: List<QuarterGradeDto>,
    val attendanceStats: AttendanceStatsDto
)
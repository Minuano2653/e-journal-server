package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceStatsDto(
    val present: Int,
    val absent: Int,
    val excused: Int
)
package com.example.models.dtos

data class TeacherScheduleRequest(
    val teacherId: String,
    val dayOfWeek: Int
)
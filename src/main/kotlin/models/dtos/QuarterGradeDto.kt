package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class QuarterGradeDto(
    val quarterId: Int,
    val averageGrade: Double?
)
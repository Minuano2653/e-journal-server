package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class StudentGradesResponse(
    val grades: List<JournalEntryDto>
)
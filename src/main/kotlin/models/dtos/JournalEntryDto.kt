package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class JournalEntryDto(
    val id: Int,
    val date: String, // ISO date format (yyyy-MM-dd)
    val grade: Int?,
    val attendanceStatus: Char?
)
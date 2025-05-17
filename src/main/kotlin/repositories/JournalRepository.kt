package com.example.repositories

import com.example.models.dtos.JournalEntryDto
import java.time.LocalDate
import java.util.*

interface JournalRepository {
    fun getStudentGrades(
        studentId: UUID,
        subjectId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<JournalEntryDto>
}
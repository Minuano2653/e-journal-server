package com.example.repositories

import com.example.models.dtos.AttendanceStatsDto
import com.example.models.dtos.JournalEntryDto
import com.example.models.dtos.QuarterGradeDto
import java.time.LocalDate
import java.util.*

interface JournalRepository {
    fun getStudentGrades(
        studentId: UUID,
        subjectId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<JournalEntryDto>

    fun getStudentPerformance(
        studentId: UUID,
        subjectId: Int
    ): Pair<List<QuarterGradeDto>, AttendanceStatsDto>
}
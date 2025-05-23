package com.example.services

import com.example.models.dtos.GroupGradesResponse
import com.example.models.dtos.JournalEntryDto
import com.example.models.dtos.StudentPerformanceResponse
import com.example.repositories.JournalRepository
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class JournalService(private val journalRepository: JournalRepository) {

    fun getStudentGradesForMonth(
        studentId: UUID,
        subjectId: Int,
        year: Int,
        month: Int
    ): List<JournalEntryDto> {
        val yearMonth = YearMonth.of(year, month)
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()

        return journalRepository.getStudentGrades(studentId, subjectId, startDate, endDate)
    }

    fun getStudentPerformance(
        studentId: UUID,
        subjectId: Int
    ): StudentPerformanceResponse {
        val (quarterGrades, attendanceStats) = journalRepository.getStudentPerformance(studentId, subjectId)
        return StudentPerformanceResponse(quarterGrades, attendanceStats)
    }

    fun getGroupGradesForDate(
        teacherId: UUID,
        groupId: UUID,
        subjectId: Int,
        date: LocalDate
    ): GroupGradesResponse? {
        return journalRepository.getGroupGradesForDate(teacherId, groupId, subjectId, date)
    }
}
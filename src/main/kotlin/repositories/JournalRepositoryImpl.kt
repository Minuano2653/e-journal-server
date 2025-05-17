package com.example.repositories

import com.example.models.dtos.AttendanceStatsDto
import com.example.models.dtos.JournalEntryDto
import com.example.models.dtos.QuarterGradeDto
import com.example.models.entities.JournalEntry
import com.example.models.entities.Quarter
import com.example.models.entities.TeacherAssignment
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class JournalRepositoryImpl: JournalRepository {
    override fun getStudentGrades(
        studentId: UUID,
        subjectId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<JournalEntryDto> = transaction {
        JournalEntry
            .join(
                TeacherAssignment, JoinType.INNER,
                onColumn = JournalEntry.assignmentId,
                otherColumn = TeacherAssignment.id
            )
            .selectAll()
            .where { (JournalEntry.studentId eq studentId) and
                        (TeacherAssignment.subjectId eq subjectId) and
                        (JournalEntry.date greaterEq startDate) and
                        (JournalEntry.date lessEq endDate)
            }
            .map {
                JournalEntryDto(
                    id = it[JournalEntry.id],
                    date = it[JournalEntry.date].format(DateTimeFormatter.ISO_DATE),
                    grade = it[JournalEntry.grade]?.toInt(),
                    attendanceStatus = it[JournalEntry.attendanceStatus]?.get(0)
                )
            }
    }

    override fun getStudentPerformance(
        studentId: UUID,
        subjectId: Int
    ): Pair<List<QuarterGradeDto>, AttendanceStatsDto> = transaction {
        val averageGrade = JournalEntry.grade.avg()

        val quarterGrades = JournalEntry
            .join(
                TeacherAssignment,
                JoinType.INNER,
                onColumn = JournalEntry.assignmentId,
                otherColumn = TeacherAssignment.id
            )
            .select(JournalEntry.quarterId, averageGrade)
            .where {
                (JournalEntry.studentId eq studentId) and
                (TeacherAssignment.subjectId eq subjectId) and
                        JournalEntry.grade.isNotNull()
            }
            .groupBy(JournalEntry.quarterId)
            .map {
                QuarterGradeDto(
                    quarterId = it[JournalEntry.quarterId],
                    averageGrade = it[averageGrade]?.toDouble()
                )
            }

        val today = LocalDate.now()
        val currentQuarterId = Quarter
            .selectAll()
            .single { today in it[Quarter.startDate]..it[Quarter.endDate] }[Quarter.id]

        // Get attendance statistics
        val attendanceCount = JournalEntry.id.count()
        val attendanceQuery = JournalEntry
            .join(
                TeacherAssignment,
                JoinType.INNER,
                onColumn = JournalEntry.assignmentId,
                otherColumn = TeacherAssignment.id
            )
            .select(JournalEntry.attendanceStatus, attendanceCount)
            .where {
                (JournalEntry.studentId eq studentId) and
                (TeacherAssignment.subjectId eq subjectId) and (JournalEntry.quarterId eq currentQuarterId)
            }
            .groupBy(JournalEntry.attendanceStatus)

        // Extract and count attendance statistics
        var present = 0
        var absent = 0
        var excused = 0

        attendanceQuery.forEach {
            when (it[JournalEntry.attendanceStatus]) {
                ATTENDANCE_PRESENT -> present = it[attendanceCount].toInt()
                ATTENDANCE_ABSENT -> absent = it[attendanceCount].toInt()
                ATTENDANCE_EXCUSED -> excused = it[attendanceCount].toInt()
            }
        }

        val attendanceStats = AttendanceStatsDto(
            present = present,
            absent = absent,
            excused = excused
        )

        Pair(quarterGrades, attendanceStats)
    }

    companion object {
        const val ATTENDANCE_PRESENT = "+"
        const val ATTENDANCE_ABSENT = "Н"
        const val ATTENDANCE_EXCUSED = "У"
    }
}
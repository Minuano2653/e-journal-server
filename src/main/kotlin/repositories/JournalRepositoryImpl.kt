package com.example.repositories

import com.example.models.dtos.JournalEntryDto
import com.example.models.entities.JournalEntry
import com.example.models.entities.TeacherAssignment
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
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
}
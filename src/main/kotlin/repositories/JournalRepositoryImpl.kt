package com.example.repositories

import com.example.models.dtos.*
import com.example.models.entities.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Suppress("LanguageDetectionInspection")
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

    override fun getGroupGradesForDate(
        teacherId: UUID,
        groupId: UUID,
        subjectId: Int,
        date: LocalDate
    ): GroupGradesResponse? = transaction {
        // Единый запрос, который получает все необходимые данные за один раз
        val result = TeacherAssignment
            .join(Group, JoinType.INNER, TeacherAssignment.groupId, Group.id)
            .join(Subject, JoinType.INNER, TeacherAssignment.subjectId, Subject.id)
            .join(Student, JoinType.INNER, Student.groupId, Group.id)
            .join(User, JoinType.INNER, Student.userId, User.id)
            .join(JournalEntry, JoinType.LEFT, additionalConstraint = {
                (JournalEntry.assignmentId eq TeacherAssignment.id) and
                        (JournalEntry.studentId eq Student.userId) and
                        (JournalEntry.date eq date)
            })
            .selectAll()
            .where {
                (TeacherAssignment.teacherId eq teacherId) and
                        (TeacherAssignment.groupId eq groupId) and
                        (TeacherAssignment.subjectId eq subjectId)
            }
            .toList()

        // Если результат пустой, значит у учителя нет доступа к этой группе/предмету
        if (result.isEmpty()) {
            return@transaction null
        }

        // Извлекаем данные из первой строки (группа и предмет одинаковые для всех строк)
        val firstRow = result.first()
        val groupName = firstRow[Group.name]
        val subjectName = firstRow[Subject.name]

        // Формируем список студентов с их оценками
        val students = result.map { row ->
            val studentId = row[Student.userId]
            val studentName = "${row[User.surname]} ${row[User.name]}" +
                    if (row[User.patronymic] != null) " ${row[User.patronymic]}" else ""

            StudentGradeDto(
                studentId = studentId.toString(),
                studentName = studentName,
                journalEntryId = row.getOrNull(JournalEntry.id),
                grade = row.getOrNull(JournalEntry.grade),
                attendanceStatus = row.getOrNull(JournalEntry.attendanceStatus)?.get(0)
            )
        }

        GroupGradesResponse(
            date = date.format(DateTimeFormatter.ISO_DATE),
            groupName = groupName,
            subjectName = subjectName,
            students = students
        )
    }

    companion object {
        const val ATTENDANCE_PRESENT = "+"
        const val ATTENDANCE_ABSENT = "Н"
        const val ATTENDANCE_EXCUSED = "У"
    }
}
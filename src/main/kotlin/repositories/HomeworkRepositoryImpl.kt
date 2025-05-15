package com.example.repositories

import com.example.models.dtos.CreateHomeworkRequest
import com.example.models.dtos.HomeworkDto
import com.example.models.dtos.UpdateHomeworkRequest
import com.example.models.entities.Homework
import com.example.models.entities.TeacherAssignment
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.UUID

class HomeworkRepositoryImpl: HomeworkRepository {

    override fun getHomeworkForTeacher(
        groupId: String,
        subjectId: Int,
        date: String,
        teacherId: UUID
    ): HomeworkDto? = transaction {

        Homework
            .join(TeacherAssignment, JoinType.INNER, TeacherAssignment.id, Homework.assignmentId)
            .selectAll()
            .where {
                (TeacherAssignment.teacherId eq teacherId) and
                (TeacherAssignment.subjectId eq subjectId) and
                (TeacherAssignment.groupId eq UUID.fromString(groupId)) and
                (Homework.date eq LocalDate.parse(date))
            }
            .map {
                HomeworkDto(
                    id = it[Homework.id],
                    assignmentId = it[Homework.assignmentId],
                    subjectId = it[TeacherAssignment.subjectId],
                    date = it[Homework.date].toString(),
                    description = it[Homework.description],
                )
            }
            .firstOrNull()

    }

    override fun createHomework(request: CreateHomeworkRequest, teacherId: String): Int = transaction {

        val teacherAssignmentId = findTeacherAssignmentId(
            teacherId,
            request.groupId,
            request.subjectId
        )

        Homework.insert {
            it[assignmentId] = teacherAssignmentId
            it[date] = LocalDate.parse(request.date)
            it[description] = request.description
        }[Homework.id]
    }

    override fun updateHomework(request: UpdateHomeworkRequest): Boolean = transaction {
        val updatedRows = Homework.update({ Homework.id eq request.homeworkId }) {
            it[description] = request.description
        }
        updatedRows > 0
    }

    private fun findTeacherAssignmentId(teacherId: String, groupId: String, subjectId: Int): Int {
        return TeacherAssignment
            .select(TeacherAssignment.id)
            .where { (TeacherAssignment.teacherId eq UUID.fromString(teacherId)) and
                    (TeacherAssignment.groupId eq UUID.fromString(groupId)) and
                    (TeacherAssignment.subjectId eq subjectId)
            }.single()[TeacherAssignment.id]
    }
}
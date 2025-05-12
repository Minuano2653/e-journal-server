package com.example.repositories

import com.example.models.dtos.CreateGroupRequest
import com.example.models.dtos.AdministratorGroupDto
import com.example.models.dtos.TeacherGroupDto
import com.example.models.entities.Group
import com.example.models.entities.Student
import com.example.models.entities.TeacherAssignment
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.*

class GroupRepositoryImpl: GroupRepository {
    override fun createGroup(request: CreateGroupRequest): UUID = transaction {
        Group.insert {
            it[name] = request.name
            it[startYear] = request.startYear
            it[endYear] = request.endYear
        }[Group.id]
    }

    override fun getTeacherGroups(teacherId: UUID): List<TeacherGroupDto> = transaction {

        val now = LocalDate.now()
        val (startYear, endYear) = if (now.monthValue < 9) {
            Pair(now.year - 1, now.year)
        } else {
            Pair(now.year, now.year + 1)
        }

        val studentCountAlias = Student.userId.countDistinct().alias("student_count")

        TeacherAssignment
            .join(Group, JoinType.INNER, TeacherAssignment.groupId, Group.id)
            .join(Student, JoinType.LEFT, Student.groupId, Group.id)
            .select(Group.name, Group.id, studentCountAlias)
            .where { (TeacherAssignment.teacherId eq teacherId) and
                    (Group.startYear eq startYear) and
                    (Group.endYear eq endYear)
            }
            .groupBy(Group.id)
            .orderBy(Group.name to SortOrder.ASC)
            .map {
                TeacherGroupDto(
                    it[Group.id].toString(),
                    it[Group.name],
                    it[studentCountAlias].toInt()
                )
            }
    }

    override fun getAllGroups(): List<AdministratorGroupDto> = transaction {
        Group
            .select(Group.id, Group.name)
            .map {
                AdministratorGroupDto(
                    it[Group.id].toString(),
                    it[Group.name]
                )
            }
    }
}
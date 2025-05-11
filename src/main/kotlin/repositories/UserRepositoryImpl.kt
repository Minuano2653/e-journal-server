package com.example.repositories

import com.example.models.dtos.CreateStudentRequest
import com.example.models.dtos.CreateTeacherRequest
import com.example.models.dtos.UserDto
import com.example.models.entities.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import java.util.*


class UserRepositoryImpl : UserRepository {
    override fun findByEmailAndPassword(email: String, password: String): UserDto? = transaction {
        User.selectAll()
            .where { (User.email eq email) and (User.password eq password) }
            .map {
                UserDto(
                    id = it[User.id],
                    email = it[User.email],
                    password = it[User.password],
                    role = it[User.roleId]
                )
            }
            .firstOrNull().also { println("Found user: $it") }
    }

    override fun createStudent(request: CreateStudentRequest): UUID = transaction {
        val userId = User.insert {
            it[name] = request.name
            it[surname] = request.surname
            it[patronymic] = request.patronymic
            it[email] = request.email
            it[password] = request.password
            it[roleId] = Role.RoleIds.STUDENT
        }[User.id]

        Student.insert {
            it[Student.userId] = userId
            it[groupId] = request.groupId?.let { groupIdStr -> UUID.fromString(groupIdStr) }
        }

        userId
    }

    override fun createTeacher(request: CreateTeacherRequest): UUID = transaction {
        val userId = User.insert {
            it[name] = request.name
            it[surname] = request.surname
            it[patronymic] = request.patronymic
            it[email] = request.email
            it[password] = request.password
            it[roleId] = Role.RoleIds.TEACHER
        }[User.id]

        Teacher.insert {
            it[Teacher.userId] = userId
        }

        TeacherAssignment.insert {
            it[teacherId] = userId
            it[groupId] = UUID.fromString(request.groupId)
            it[subjectId] = request.subjectId
        }

        userId
    }
}
package com.example.repositories

import com.example.models.dtos.CreateStudentRequest
import com.example.models.dtos.CreateTeacherRequest
import com.example.models.dtos.UserDto
import java.util.*

interface UserRepository {
    fun findByEmailAndPassword(email: String, password: String): UserDto?
    fun findStudentGroupId(studentId: UUID): UUID?
    fun createStudent(request: CreateStudentRequest): UUID
    fun createTeacher(request: CreateTeacherRequest): UUID
}
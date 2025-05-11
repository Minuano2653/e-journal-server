package com.example.services

import com.example.models.dtos.CreateStudentRequest
import com.example.models.dtos.CreateTeacherRequest
import com.example.models.entities.Role
import com.example.repositories.UserRepository
import java.util.*

class UserService(private val userRepository: UserRepository) {

    fun createStudent(request: CreateStudentRequest, roleId: Int): UUID? {
        return if (roleId == Role.RoleIds.ADMINISTRATOR) {
            userRepository.createStudent(request)
        } else null
    }

    fun createTeacher(request: CreateTeacherRequest, roleId: Int): UUID? {
        return if (roleId == Role.RoleIds.ADMINISTRATOR) {
            userRepository.createTeacher(request)
        } else null
    }
}
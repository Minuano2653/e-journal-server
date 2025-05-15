package com.example.services

import com.example.models.dtos.CreateHomeworkRequest
import com.example.models.dtos.HomeworkDto
import com.example.models.dtos.UpdateHomeworkRequest
import com.example.models.entities.Role
import com.example.repositories.HomeworkRepository
import java.util.*

class HomeworkService(private val homeworkRepository: HomeworkRepository) {
    fun createHomework(request: CreateHomeworkRequest, teacherId: String, roleId: Int): Int? {
        return if (roleId == Role.RoleIds.TEACHER) {
            homeworkRepository.createHomework(request, teacherId)
        } else null
    }

    fun updateHomework(request: UpdateHomeworkRequest, roleId: Int): Boolean {
        return if (roleId == Role.RoleIds.TEACHER) {
            homeworkRepository.updateHomework(request)
        } else false
    }

    fun getHomeworkForTeacher(groupId: String, subjectId: Int, date: String, teacherId: UUID, roleId: Int): HomeworkDto? {
        return if (roleId == Role.RoleIds.TEACHER) {
            homeworkRepository.getHomeworkForTeacher(groupId, subjectId, date, teacherId)
        } else null
    }
}
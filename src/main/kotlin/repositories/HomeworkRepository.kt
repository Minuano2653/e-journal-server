package com.example.repositories

import com.example.models.dtos.CreateHomeworkRequest
import com.example.models.dtos.HomeworkDto
import com.example.models.dtos.UpdateHomeworkRequest
import java.util.*

interface HomeworkRepository {
    fun getHomeworkForTeacher(groupId: String, subjectId: Int, date: String, teacherId: UUID): HomeworkDto?
    fun createHomework(request: CreateHomeworkRequest, teacherId: String): Int
    fun updateHomework(request: UpdateHomeworkRequest): Boolean
}
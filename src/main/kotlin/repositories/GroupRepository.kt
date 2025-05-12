package com.example.repositories

import com.example.models.dtos.CreateGroupRequest
import com.example.models.dtos.AdministratorGroupDto
import com.example.models.dtos.TeacherGroupDto
import java.util.*

interface GroupRepository {
    fun createGroup(request: CreateGroupRequest): UUID
    fun getTeacherGroups(teacherId: UUID): List<TeacherGroupDto>
    fun getAllGroups(): List<AdministratorGroupDto>
}
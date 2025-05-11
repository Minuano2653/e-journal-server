package com.example.services

import com.example.models.dtos.CreateGroupRequest
import com.example.repositories.GroupRepository
import java.util.*

class GroupService(private val groupRepository: GroupRepository) {
    fun createGroup(request: CreateGroupRequest, roleId: Int): UUID? {
        return if (roleId == 3) {
            groupRepository.createGroup(request)
        } else null
    }
}
package com.example.repositories

import com.example.models.dtos.CreateGroupRequest
import java.util.*

interface GroupRepository {
    fun createGroup(request: CreateGroupRequest): UUID
}
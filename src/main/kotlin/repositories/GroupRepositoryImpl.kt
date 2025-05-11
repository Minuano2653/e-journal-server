package com.example.repositories

import com.example.models.dtos.CreateGroupRequest
import com.example.models.entities.Group
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class GroupRepositoryImpl: GroupRepository {
    override fun createGroup(request: CreateGroupRequest): UUID = transaction {
        Group.insert {
            it[name] = request.name
            it[startYear] = request.startYear
            it[endYear] = request.endYear
        }[Group.id]
    }
}
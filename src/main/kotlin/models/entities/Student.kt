package com.example.models.entities

import org.jetbrains.exposed.sql.Table

object Student : Table("student") {
    val userId = uuid("user_id").references(User.id)
    val groupId = uuid("group_id").references(Group.id).nullable()
    override val primaryKey = PrimaryKey(userId)
}

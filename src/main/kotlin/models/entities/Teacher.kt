package com.example.models.entities

import org.jetbrains.exposed.sql.Table

object Teacher : Table("teacher") {
    val userId = uuid("user_id").references(User.id)
    override val primaryKey = PrimaryKey(userId)
}
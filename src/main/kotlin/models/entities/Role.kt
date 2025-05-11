package com.example.models.entities

import org.jetbrains.exposed.sql.Table

object Role : Table("role") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    override val primaryKey = PrimaryKey(id)

    object RoleIds {
        const val STUDENT = 1
        const val TEACHER = 2
        const val ADMINISTRATOR = 3
    }
}
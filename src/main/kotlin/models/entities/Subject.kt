package com.example.models.entities

import org.jetbrains.exposed.sql.Table

object Subject : Table("subject") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    override val primaryKey = PrimaryKey(id)
}
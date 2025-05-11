package com.example.models.entities

import com.example.models.dtos.UserDto
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object User : Table("user") {
    val id = uuid("id")
    val roleId = integer("role_id")  // переименовано в соответствии с БД
    val name = varchar("name", 100)
    val surname = varchar("surname", 100)
    val patronymic = varchar("patronymic", 100).nullable()
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    override val primaryKey = PrimaryKey(id)
}
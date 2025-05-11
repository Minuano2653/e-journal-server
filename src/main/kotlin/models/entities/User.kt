package com.example.models.entities

import com.example.models.entities.Group.clientDefault
import org.jetbrains.exposed.sql.Table
import java.util.*

object User : Table("user") {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val roleId = integer("role_id")
    val name = varchar("name", 100)
    val surname = varchar("surname", 100)
    val patronymic = varchar("patronymic", 100).nullable()
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    override val primaryKey = PrimaryKey(id)
}
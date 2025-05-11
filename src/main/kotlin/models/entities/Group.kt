package com.example.models.entities

import org.jetbrains.exposed.sql.Table
import java.util.*

object Group : Table("group") {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val name = varchar("name", 50)
    val startYear = integer("start_year")
    val endYear = integer("end_year")
    override val primaryKey = PrimaryKey(id)
}
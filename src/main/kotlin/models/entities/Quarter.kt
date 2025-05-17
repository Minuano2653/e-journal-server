package com.example.models.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date


object Quarter: Table("quarter") {
    val id = integer("id").autoIncrement()
    val startDate = date("start_date")
    val endDate = date("end_date")

    override val primaryKey = PrimaryKey(id)
}
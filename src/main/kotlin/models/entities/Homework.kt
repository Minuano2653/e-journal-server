package com.example.models.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Homework: Table("homework") {
    val id = integer("id").autoIncrement()
    val assignmentId = integer("assignment_id").references(TeacherAssignment.id)
    val date = date("date")
    val description = text("description")
}
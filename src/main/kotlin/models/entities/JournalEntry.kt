package com.example.models.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object JournalEntry: Table("journal_entry") {
    val id = integer("id").autoIncrement()
    val assignmentId = integer("assignment_id").references(TeacherAssignment.id)
    val studentId = uuid("student_id").references(Student.userId)
    val quarterId = integer("quarter_id").references(Quarter.id)
    val date = date("date")
    val grade = integer("grade").nullable()
    val attendanceStatus = varchar("attendance_status", 50).nullable()

    override val primaryKey = PrimaryKey(id)
}
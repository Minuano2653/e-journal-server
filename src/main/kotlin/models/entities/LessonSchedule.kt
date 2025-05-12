package com.example.models.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time

object LessonSchedule : Table("lesson_schedule") {
    val id = integer("id").autoIncrement()
    val assignmentId = integer("assignment_id").references(TeacherAssignment.id)
    val classroom = varchar("classroom", 50)
    val dayOfWeek = integer("day_of_week")
    val lessonNumber = integer("lesson_number")
    val startTime = time("start_time")
    val endTime = time("end_time")

    override val primaryKey = PrimaryKey(id)
}
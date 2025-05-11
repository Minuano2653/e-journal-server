package com.example.models.entities

import org.jetbrains.exposed.sql.Table

object TeacherAssignment : Table("teacher_assignment") {
    val id = integer("id").autoIncrement()
    val teacherId = uuid("teacher_id").references(Teacher.userId)
    val groupId = uuid("group_id").references(Group.id)
    val subjectId = integer("subject_id").references(Subject.id)
    override val primaryKey = PrimaryKey(id)
}
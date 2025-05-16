package com.example.repositories

import com.example.models.dtos.GroupLessonDto
import com.example.models.dtos.TeacherLessonDto
import java.time.LocalDate
import java.util.*

interface ScheduleRepository {
    fun getGroupScheduleForDay(groupId: UUID, date: LocalDate): List<GroupLessonDto>
    fun getTeacherScheduleForDay(teacherId: UUID, dayOfWeek: Int): List<TeacherLessonDto>
}
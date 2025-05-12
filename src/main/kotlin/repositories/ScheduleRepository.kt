package com.example.repositories

import com.example.models.dtos.GroupLessonDto
import com.example.models.dtos.TeacherLessonDto
import java.util.*

interface ScheduleRepository {
    fun getGroupScheduleForDay(groupId: UUID, dayOfWeek: Int): List<GroupLessonDto>
    fun getTeacherScheduleForDay(teacherId: UUID, dayOfWeek: Int): List<TeacherLessonDto>
}
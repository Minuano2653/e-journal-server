package com.example.services

import com.example.models.dtos.GroupScheduleResponse
import com.example.models.dtos.TeacherScheduleResponse
import com.example.repositories.ScheduleRepository
import java.util.*


class ScheduleService(
    private val scheduleRepository: ScheduleRepository) {
    fun getGroupScheduleForDay(groupId: UUID, dayOfWeek: Int): GroupScheduleResponse {
        val lessons = scheduleRepository.getGroupScheduleForDay(groupId, dayOfWeek)
        return GroupScheduleResponse(lessons)
    }

    fun getTeacherScheduleForDay(teacherId: UUID, dayOfWeek: Int): TeacherScheduleResponse {
        val lessons = scheduleRepository.getTeacherScheduleForDay(teacherId, dayOfWeek)
        return TeacherScheduleResponse(lessons)
    }
}
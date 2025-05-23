package com.example.services

import com.example.models.dtos.GroupScheduleResponse
import com.example.models.dtos.TeacherScheduleResponse
import com.example.repositories.ScheduleRepository
import java.time.LocalDate
import java.util.*


class ScheduleService(
    private val scheduleRepository: ScheduleRepository) {
    fun getGroupScheduleWithHomeworkForDay(groupId: UUID, date: LocalDate): GroupScheduleResponse {
        val lessons = scheduleRepository.getGroupScheduleForDay(groupId, date)
        return GroupScheduleResponse(lessons)
    }

    fun getTeacherScheduleForDay(teacherId: UUID, date: LocalDate): TeacherScheduleResponse {
        val lessons = scheduleRepository.getTeacherScheduleForDay(teacherId, date)
        return TeacherScheduleResponse(lessons)
    }
}
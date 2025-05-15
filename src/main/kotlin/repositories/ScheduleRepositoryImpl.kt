package com.example.repositories

import com.example.models.dtos.*
import com.example.models.entities.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class ScheduleRepositoryImpl: ScheduleRepository {
    override fun getGroupScheduleForDay(groupId: UUID, dayOfWeek: Int): List<GroupLessonDto> = transaction {
        LessonSchedule
            .join(TeacherAssignment, JoinType.INNER, LessonSchedule.assignmentId, TeacherAssignment.id)
            .join(Subject, JoinType.INNER, TeacherAssignment.subjectId, Subject.id)
            .join(User, JoinType.INNER, TeacherAssignment.teacherId, User.id)
            .selectAll()
            .where {
                (TeacherAssignment.groupId eq groupId) and (LessonSchedule.dayOfWeek eq dayOfWeek)
            }
            .orderBy(LessonSchedule.lessonNumber)
            .map {
                GroupLessonDto(
                    lessonNumber = it[LessonSchedule.lessonNumber],
                    startTime = it[LessonSchedule.startTime].toString(),
                    endTime = it[LessonSchedule.endTime].toString(),
                    subject = it[Subject.name],
                    teacher = "${it[User.surname]} ${it[User.name].first()}. ${it[User.patronymic]?.first() ?: ""}.",
                    classroom = it[LessonSchedule.classroom]
                )
            }
    }

    override fun getTeacherScheduleForDay(teacherId: UUID, dayOfWeek: Int): List<TeacherLessonDto> = transaction {
        (LessonSchedule
            .join(TeacherAssignment, JoinType.INNER, LessonSchedule.assignmentId, TeacherAssignment.id)
            .join(Subject, JoinType.INNER, TeacherAssignment.subjectId, Subject.id)
            .join(Group, JoinType.INNER, TeacherAssignment.groupId, Group.id))
            .selectAll()
            .where {
                (TeacherAssignment.teacherId eq teacherId) and (LessonSchedule.dayOfWeek eq dayOfWeek)
            }
            .orderBy(LessonSchedule.lessonNumber)
            .map {
                TeacherLessonDto(
                    lessonNumber = it[LessonSchedule.lessonNumber],
                    startTime = it[LessonSchedule.startTime].toString(),
                    endTime = it[LessonSchedule.endTime].toString(),
                    group = GroupDto(it[Group.id].toString(), it[Group.name]),
                    subject = SubjectDto(it[Subject.id].toString(), it[Subject.name]),
                    classroom = it[LessonSchedule.classroom]
                )
            }
    }
}
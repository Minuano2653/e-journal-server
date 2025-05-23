package com.example.repositories

import com.example.models.dtos.*
import com.example.models.entities.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.*

class ScheduleRepositoryImpl: ScheduleRepository {
    override fun getGroupScheduleForDay(groupId: UUID, date: LocalDate): List<GroupLessonDto> = transaction {
        /*val dayOfWeek = date.dayOfWeek.value

        val homeworkAlias = Homework.alias("h")

        LessonSchedule
            .join(TeacherAssignment, JoinType.INNER, LessonSchedule.assignmentId, TeacherAssignment.id)
            .join(Subject, JoinType.INNER, TeacherAssignment.subjectId, Subject.id)
            .join(User, JoinType.INNER, TeacherAssignment.teacherId, User.id)
            .join(homeworkAlias, JoinType.LEFT,
                onColumn = LessonSchedule.assignmentId,
                otherColumn = homeworkAlias[Homework.assignmentId]
            )
            .selectAll().where {
                (TeacherAssignment.groupId eq groupId) and
                        (LessonSchedule.dayOfWeek eq dayOfWeek) and
                        ((homeworkAlias[Homework.date] eq date) or (homeworkAlias[Homework.date].isNull()))
            }
            .orderBy(LessonSchedule.lessonNumber)
            .map {
                GroupLessonDto(
                    lessonNumber = it[LessonSchedule.lessonNumber],
                    startTime = it[LessonSchedule.startTime].toString(),
                    endTime = it[LessonSchedule.endTime].toString(),
                    subject = it[Subject.name],
                    teacher = "${it[User.surname]} ${it[User.name].first()}. ${it[User.patronymic]?.first() ?: ""}.",
                    classroom = it[LessonSchedule.classroom],
                    homework = it[homeworkAlias[Homework.description]]
                )
            }*/
        LessonSchedule
            .join(TeacherAssignment, JoinType.INNER, LessonSchedule.assignmentId, TeacherAssignment.id)
            .join(Subject, JoinType.INNER, TeacherAssignment.subjectId, Subject.id)
            .join(User, JoinType.INNER, TeacherAssignment.teacherId, User.id)
            .join(Homework, JoinType.LEFT, LessonSchedule.assignmentId, Homework.assignmentId, additionalConstraint =
                {Homework.date eq date})
            .selectAll()
            .where {
                (TeacherAssignment.groupId eq groupId) and
                (LessonSchedule.dayOfWeek eq date.dayOfWeek.value)
            }
            .orderBy(LessonSchedule.lessonNumber)
            .map {
                GroupLessonDto(
                    lessonNumber = it[LessonSchedule.lessonNumber],
                    startTime = it[LessonSchedule.startTime].toString(),
                    endTime = it[LessonSchedule.endTime].toString(),
                    subject = it[Subject.name],
                    teacher = "${it[User.surname]} ${it[User.name].first()}. ${it[User.patronymic]?.first() ?: ""}.",
                    classroom = it[LessonSchedule.classroom],
                    homework = if (it[Homework.date] == date) it[Homework.description] else null

                )
            }
    }

    override fun getTeacherScheduleForDay(teacherId: UUID, date: LocalDate): List<TeacherLessonDto> = transaction {
        (LessonSchedule
            .join(TeacherAssignment, JoinType.INNER, LessonSchedule.assignmentId, TeacherAssignment.id)
            .join(Subject, JoinType.INNER, TeacherAssignment.subjectId, Subject.id)
            .join(Group, JoinType.INNER, TeacherAssignment.groupId, Group.id))
            .selectAll()
            .where {
                (TeacherAssignment.teacherId eq teacherId) and (LessonSchedule.dayOfWeek eq date.dayOfWeek.value)
            }
            .orderBy(LessonSchedule.lessonNumber)
            .map {
                TeacherLessonDto(
                    lessonNumber = it[LessonSchedule.lessonNumber],
                    startTime = it[LessonSchedule.startTime].toString(),
                    endTime = it[LessonSchedule.endTime].toString(),
                    group = GroupDto(it[Group.id].toString(), it[Group.name]),
                    subject = SubjectDto(it[Subject.id], it[Subject.name]),
                    classroom = it[LessonSchedule.classroom]
                )
            }
    }
}
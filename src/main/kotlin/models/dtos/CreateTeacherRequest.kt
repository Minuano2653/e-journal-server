package com.example.models.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTeacherRequest(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val patronymic: String? = null,
    @SerialName("group_id")
    val groupId: String? = null, // В списке можно будет выбрать группу, которая и содержит свой id
    @SerialName("subject_id")
    val subjectId: Int // В списке можно будет выбрать предмет, который и содержит свой id
)
package com.example.models.dtos

import kotlinx.serialization.Serializable


@Serializable
data class GroupScheduleRequest(
    val groupId: String,
    val dayOfWeek: Int
)
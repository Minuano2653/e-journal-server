package com.example.models.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequest(
    val name: String,
    @SerialName("start_year")
    val startYear: Int,
    @SerialName("end_year")
    val endYear: Int
)
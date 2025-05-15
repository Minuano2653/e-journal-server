package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SubjectDto(
    val id: String,
    val name: String
)
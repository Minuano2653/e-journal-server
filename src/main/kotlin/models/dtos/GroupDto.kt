package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val id: String,
    val name: String
)
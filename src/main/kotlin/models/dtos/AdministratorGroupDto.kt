package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AdministratorGroupDto(
    val id: String,
    val name: String
)
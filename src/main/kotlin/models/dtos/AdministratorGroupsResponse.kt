package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AdministratorGroupsResponse(val groups: List<AdministratorGroupDto>)
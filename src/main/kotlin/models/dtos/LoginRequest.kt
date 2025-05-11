package com.example.models.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)
package com.example.services

import com.auth0.jwt.JWT
import com.example.models.dtos.LoginRequest
import com.example.repositories.UserRepository
import com.example.security.JwtConfig
import java.util.*

class AuthService(private val userRepository: UserRepository) {
    fun login(request: LoginRequest): String? {
        val user = userRepository.findByEmailAndPassword(request.email, request.password)
        return if (user != null) {
            generateToken(user.id, user.role)
        } else null
    }

    private fun generateToken(userId: UUID, roleId: Int): String {
        return JWT.create()
            .withSubject("Authentication")
            .withIssuer(JwtConfig.ISSUER)
            .withAudience(JwtConfig.AUDIENCE)
            .withClaim(JwtConfig.USER_ID_CLAIM, userId.toString()) // UUID как строка
            .withClaim(JwtConfig.ROLE_ID_CLAIM, roleId)
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000))
            .sign(JwtConfig.getAlgorithm())
    }

}
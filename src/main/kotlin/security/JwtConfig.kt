package com.example.security

object JwtConfig {
    const val SECRET = "secret"
    const val ISSUER = "ktor-issuer"
    const val AUDIENCE = "ktor-audience"
    const val REALM = "ktor sample app"

    const val USER_ID_CLAIM = "USER_ID"
    const val ROLE_ID_CLAIM = "ROLE_ID"

    fun getAlgorithm() = com.auth0.jwt.algorithms.Algorithm.HMAC256(SECRET)
}
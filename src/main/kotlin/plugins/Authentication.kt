package com.example.plugins

import com.auth0.jwt.JWT
import com.example.security.JwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = JwtConfig.REALM
            verifier(
                JWT
                    .require(JwtConfig.getAlgorithm())
                    .withAudience(JwtConfig.AUDIENCE)
                    .withIssuer(JwtConfig.ISSUER)
                    .build()
            )
            validate { credential ->
                val userId = credential.payload.getClaim(JwtConfig.USER_ID_CLAIM).asString()
                val roleId = credential.payload.getClaim(JwtConfig.ROLE_ID_CLAIM).asInt()
                println("UserId: $userId, RoleId: $roleId")
                if (!userId.isNullOrEmpty()) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
package com.example.utils

import com.example.security.JwtConfig
import io.ktor.server.auth.jwt.*
import java.util.*

fun JWTPrincipal.getUserId(): UUID =
    UUID.fromString(payload.getClaim(JwtConfig.USER_ID_CLAIM).asString())

fun JWTPrincipal.getRoleId(): Int =
    payload.getClaim(JwtConfig.ROLE_ID_CLAIM).asInt()
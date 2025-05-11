package com.example.repositories

import com.example.models.dtos.UserDto
import com.example.models.entities.User
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll


class UserRepositoryImpl : UserRepository {
    override fun findByEmailAndPassword(email: String, password: String): UserDto? = transaction {
        User.selectAll()
            .where { (User.email eq email) and (User.password eq password) }
            .map {
                UserDto(
                    id = it[User.id],
                    email = it[User.email],
                    password = it[User.password],
                    role = it[User.roleId]
                )
            }
            .firstOrNull().also { println("Found user: $it") }
    }
}
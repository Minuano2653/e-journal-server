package com.example

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        val config = ConfigFactory.load().getConfig("database")

        val hikariConfig = HikariConfig().apply {
            driverClassName = config.getString("driver")
            jdbcUrl = config.getString("url")
            username = config.getString("user")
            password = config.getString("password")
            maximumPoolSize = config.getInt("maximumPoolSize")
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        Database.connect(HikariDataSource(hikariConfig))
    }
}
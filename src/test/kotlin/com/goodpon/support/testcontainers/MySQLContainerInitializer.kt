package com.goodpon.support.testcontainers

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MySQLContainer

class MySQLContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    companion object {
        private val MYSQL = MySQLContainer("mysql:8.0.42")

        init {
            MYSQL.start()
        }
    }

    override fun initialize(context: ConfigurableApplicationContext) {
        val properties = mapOf(
            "spring.datasource.url" to MYSQL.jdbcUrl,
            "spring.datasource.username" to MYSQL.username,
            "spring.datasource.password" to MYSQL.password
        )

        TestPropertyValues.of(properties).applyTo(context)
    }
}
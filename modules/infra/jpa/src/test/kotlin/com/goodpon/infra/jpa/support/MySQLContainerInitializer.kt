package com.goodpon.infra.jpa.support

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MySQLContainer

class MySQLContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {
        val properties = mapOf(
            "spring.datasource.url" to MYSQL_CONTAINER.jdbcUrl,
            "spring.datasource.username" to MYSQL_CONTAINER.username,
            "spring.datasource.password" to MYSQL_CONTAINER.password
        )
        TestPropertyValues.of(properties).applyTo(context)
    }

    companion object {
        private val MYSQL_CONTAINER = MySQLContainer("mysql:8.4")

        init {
            MYSQL_CONTAINER.start()
        }
    }
}
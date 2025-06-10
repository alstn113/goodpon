package com.goodpon.support.testcontainers

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer

class RedisContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    companion object {
        private val REDIS = GenericContainer("valkey/valkey:8.0.1")
            .withExposedPorts(6379)

        init {
            com.goodpon.support.testcontainers.RedisContainerInitializer.Companion.REDIS.start()
        }
    }

    override fun initialize(context: ConfigurableApplicationContext) {
        val properties: Map<String, String> = mapOf(
            "spring.data.redis.host" to com.goodpon.support.testcontainers.RedisContainerInitializer.Companion.REDIS.host,
            "spring.data.redis.port" to com.goodpon.support.testcontainers.RedisContainerInitializer.Companion.REDIS.firstMappedPort.toString(),
            "spring.data.redis.ssl.enabled" to false.toString()
        )

        TestPropertyValues.of(properties).applyTo(context)
    }
}
package com.goodpon.infra.kafka

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.kafka.ConfluentKafkaContainer

class KafkaContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val properties = mapOf(
            "spring.kafka.bootstrap-servers" to KAFKA_CONTAINER.bootstrapServers
        )
        TestPropertyValues.of(properties).applyTo(applicationContext)
    }

    companion object {
        val KAFKA_CONTAINER = ConfluentKafkaContainer("confluentinc/cp-kafka:7.5.3")

        init {
            KAFKA_CONTAINER.start()
        }
    }
}
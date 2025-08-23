package com.goodpon.infra.kafka

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName

class KafkaContainerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        // Empty
    }

    companion object {
        val KAFKA_CONTAINER = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.3"))

        init {
            KAFKA_CONTAINER.start()
        }
    }
}
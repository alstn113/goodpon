package com.goodpon.infra.kafka.config

import org.apache.kafka.common.TopicPartition
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import java.time.Duration

@Configuration
class KafkaConfig {

    @Bean
    fun producerFactory(
        kafkaProperties: KafkaProperties,
    ): ProducerFactory<Any, Any> {
        val props: Map<String, Any> = HashMap(kafkaProperties.buildProducerProperties())
        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun consumerFactory(
        kafkaProperties: KafkaProperties,
    ): ConsumerFactory<Any, Any> {
        val props = HashMap<String, Any>(kafkaProperties.buildConsumerProperties())
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<Any, Any>): KafkaTemplate<Any, Any> {
        return KafkaTemplate(producerFactory)
    }

    @Bean
    fun defaultKafkaListenerContainerFactoryConfig(
        kafkaProperties: KafkaProperties,
        kafkaTemplate: KafkaTemplate<Any, Any>,
    ): ConcurrentKafkaListenerContainerFactory<*, *> {
        val consumerConfig = HashMap(kafkaProperties.buildConsumerProperties())
        return ConcurrentKafkaListenerContainerFactory<Any, Any>().apply {
            this.consumerFactory = DefaultKafkaConsumerFactory(consumerConfig)
            this.setCommonErrorHandler(defaultErrorHandler(kafkaTemplate))
            this.isBatchListener = false
            this.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }
    }

    @Bean
    fun defaultErrorHandler(
        kafkaTemplate: KafkaTemplate<Any, Any>,
    ): DefaultErrorHandler {
        val recoverer = DeadLetterPublishingRecoverer(kafkaTemplate) { record, _ ->
            TopicPartition(record.topic() + DLT_SUFFIX, record.partition())
        }

        val backOff = ExponentialBackOffWithJitter(Duration.ofSeconds(1), 2.0, Duration.ofSeconds(16), 3)

        return DefaultErrorHandler(recoverer, backOff)
    }

    companion object {
        const val DLT_SUFFIX = ".DLT"
    }
}
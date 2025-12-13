package com.goodpon.consumer.couponissuer.listener.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DefaultErrorHandler
import java.time.Duration

@Configuration
class KafkaConsumerConfig {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean(name = [COUPON_ISSUER_LISTENER_CONTAINER_FACTORY])
    fun couponIssuerKafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        defaultErrorHandler: DefaultErrorHandler,
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            this.consumerFactory = consumerFactory
            this.setCommonErrorHandler(defaultErrorHandler)
            isBatchListener = false
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        }
    }

    @Bean
    fun consumerFactory(kafkaProperties: KafkaProperties): ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties())
    }

    @Bean
    fun defaultErrorHandler(
        couponIssueFailureRecoverer: CouponIssueFailureRecoverer,
    ): DefaultErrorHandler {
        val backOff = ExponentialBackOffWithJitter(
            initialInterval = Duration.ofSeconds(1),
            multiplier = 2.0,
            maxInterval = Duration.ofSeconds(10),
            maxAttempts = 5,
        )

        val errorHandler = DefaultErrorHandler(couponIssueFailureRecoverer, backOff)
        errorHandler.setRetryListeners({ record, ex, attempt ->
            log.warn("[RetryListener] attempt=$attempt, record=$record, exception=${ex.message}")
        })

        return errorHandler
    }

    companion object {
        const val COUPON_ISSUER_LISTENER_CONTAINER_FACTORY = "couponIssuerKafkaListenerContainerFactory"
    }
}
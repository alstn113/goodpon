package com.goodpon.couponissuer.worker

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.couponissuer.application.port.`in`.IssueCouponUseCase
import com.goodpon.couponissuer.worker.config.KafkaConsumerConfig
import com.goodpon.couponissuer.worker.dto.IssueCouponRequestedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CouponKafkaEventListener(
    private val issueCouponUseCase: IssueCouponUseCase,
    private val objectMapper: ObjectMapper,
) {

    @KafkaListener(
        topics = ["issue-coupon-requested"],
        containerFactory = KafkaConsumerConfig.Companion.COUPON_ISSUER_LISTENER_CONTAINER_FACTORY
    )
    fun handleIssueCouponRequestedEvent(
        @Payload eventJsonString: String,
        acknowledgment: Acknowledgment,
    ) {
        val event = objectMapper.readValue(eventJsonString, IssueCouponRequestedEvent::class.java)
        val command = event.toIssueCouponCommand()

        issueCouponUseCase(command)

        acknowledgment.acknowledge()
    }
}
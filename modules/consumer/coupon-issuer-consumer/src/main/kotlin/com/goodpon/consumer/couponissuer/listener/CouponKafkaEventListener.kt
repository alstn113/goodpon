package com.goodpon.consumer.couponissuer.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.application.couponissuer.port.`in`.IssueCouponUseCase
import com.goodpon.application.couponissuer.port.`in`.dto.IssueCouponCommand
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CouponKafkaEventListener(
    private val issueCouponUseCase: IssueCouponUseCase,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(
        topics = ["issue-coupon-requested"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun handleIssueCouponRequestedEvent(eventJsonString: String) {
        val event = objectMapper.readValue(eventJsonString, IssueCouponRequestedEvent::class.java)
        val command = IssueCouponCommand(
            couponTemplateId = event.couponTemplateId,
            userId = event.userId,
            requestedAt = event.requestedAt
        )
        issueCouponUseCase(command)
    }
}
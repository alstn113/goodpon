package com.goodpon.partner.infra.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.infra.kafka.config.KafkaTopic
import com.goodpon.partner.application.coupon.port.out.CouponEventPublisher
import com.goodpon.partner.application.coupon.port.out.dto.IssueCouponRequestedEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CouponEventKafkaPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) : CouponEventPublisher {

    override fun publishIssueCouponRequested(event: IssueCouponRequestedEvent) {
        val jsonString = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(
            KafkaTopic.ISSUE_COUPON_REQUESTED.topicName,
            event.userId, // 파티션 키
            jsonString
        )
    }
}
package com.goodpon.consumer.couponissuer.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.application.couponissuer.port.`in`.HasValidReservationUseCase
import com.goodpon.application.couponissuer.port.`in`.IssueCouponUseCase
import com.goodpon.consumer.couponissuer.listener.config.KafkaConsumerConfig
import com.goodpon.consumer.couponissuer.listener.dto.IssueCouponRequestedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class CouponKafkaEventListener(
    private val hasValidReservationUseCase: HasValidReservationUseCase,
    private val issueCouponUseCase: IssueCouponUseCase,
    private val objectMapper: ObjectMapper,
) {

    @KafkaListener(
        topics = ["issue-coupon-requested"],
        containerFactory = KafkaConsumerConfig.COUPON_ISSUER_LISTENER_CONTAINER_FACTORY
    )
    fun handleIssueCouponRequestedEvent(
        @Payload eventJsonString: String,
        acknowledgment: Acknowledgment,
    ) {
        val event = objectMapper.readValue(eventJsonString, IssueCouponRequestedEvent::class.java)
        val command = event.toIssueCouponCommand()

        // 선점된(Reserved) 쿠폰이 없는 경우 무시
        if (hasValidReservationUseCase(couponTemplateId = command.couponTemplateId, userId = command.userId)) {
            issueCouponUseCase(command)
        }

        acknowledgment.acknowledge()
    }
}
package com.goodpon.couponissuer.worker.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.couponissuer.application.port.`in`.MarkIssueReservationAsFailedUseCase
import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.ConsumerRecordRecoverer
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.stereotype.Component

@Component
class CouponIssueFailureRecoverer(
    kafkaTemplate: KafkaTemplate<String, String>,
    private val markIssueReservationAsFailedUseCase: MarkIssueReservationAsFailedUseCase,
    private val objectMapper: ObjectMapper,
) : ConsumerRecordRecoverer {

    private val log = LoggerFactory.getLogger(javaClass)

    private val dltRecoverer = DeadLetterPublishingRecoverer(kafkaTemplate) { record, ex ->
        log.error("[DeadLetter] record=$record, exception=${ex.message}")
        TopicPartition(record.topic() + DLT_SUFFIX, record.partition())
    }

    override fun accept(record: ConsumerRecord<*, *>, exception: Exception) {
        try {
            val value = record.value().toString()
            val command = objectMapper.readValue(value, IssueCouponCommand::class.java)

            markIssueReservationAsFailedUseCase(couponTemplateId = command.couponTemplateId, userId = command.userId)
        } catch (e: Exception) {
            log.error("쿠폰 발급 처리 중 오류가 발생했고, Redis Reserved 상태 변경 처리도 실패했습니다. record=$record", e)
        }

        dltRecoverer.accept(record, exception)
    }

    companion object {
        const val DLT_SUFFIX = ".DLT"
    }
}
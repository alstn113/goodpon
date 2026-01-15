package com.goodpon.couponissuer.worker

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.couponissuer.application.port.`in`.IssueCouponBatchUseCase
import com.goodpon.couponissuer.application.port.`in`.IssueCouponUseCase
import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand
import com.goodpon.couponissuer.worker.config.CouponIssueFailureRecoverer
import com.goodpon.couponissuer.worker.config.KafkaConsumerConfig
import com.goodpon.couponissuer.worker.dto.IssueCouponRequestedEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class CouponKafkaEventListener(
    private val issueCouponUseCase: IssueCouponUseCase,
    private val issueCouponBatchUseCase: IssueCouponBatchUseCase,
    private val objectMapper: ObjectMapper,
    private val recoverer: CouponIssueFailureRecoverer,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = ["issue-coupon-requested"],
        containerFactory = KafkaConsumerConfig.COUPON_ISSUER_LISTENER_CONTAINER_FACTORY
    )
    fun handleIssueCouponRequestedEvent(
        records: List<ConsumerRecord<String, String>>,
        acknowledgment: Acknowledgment,
    ) {
        val commands = convertToCommands(records)

        try {
            issueCouponBatchUseCase(commands)
        } catch (e: Exception) {
            log.warn("issue Coupon 배처 처리에 실패하여 단건 처리로 fallback 합니다. reason=${e.message}")

            fallbackToSingleProcessing(records, commands)
        } finally {
            acknowledgment.acknowledge()
        }
    }

    private fun convertToCommands(records: List<ConsumerRecord<String, String>>): List<IssueCouponCommand> {
        return records.map { record ->
            val event = objectMapper.readValue(record.value(), IssueCouponRequestedEvent::class.java)
            event.toIssueCouponCommand()
        }
    }

    private fun fallbackToSingleProcessing(
        records: List<ConsumerRecord<String, String>>,
        commands: List<IssueCouponCommand>,
    ) {
        records.forEachIndexed { index, record ->
            val command = commands[index]
            processSingleRecord(record, command)
        }
    }

    private fun processSingleRecord(
        record: ConsumerRecord<String, String>,
        command: IssueCouponCommand,
    ) {
        try {
            issueCouponUseCase(command)
        } catch (ex: Exception) {
            log.error("단건 쿠폰 발급 처리 중 오류가 발생했습니다. record=$record, exception=${ex.message}")
            recoverer.accept(record, ex)
        }
    }
}
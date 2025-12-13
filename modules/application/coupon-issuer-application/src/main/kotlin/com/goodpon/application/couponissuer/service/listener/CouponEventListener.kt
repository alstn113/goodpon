package com.goodpon.application.couponissuer.service.listener

import com.goodpon.application.couponissuer.port.out.CouponTemplateStatsCache
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CouponEventListener(
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleCouponIssuedEvent(event: CouponIssuedEvent) {
        try {
            couponTemplateStatsCache.completeIssueCoupon(
                couponTemplateId = event.couponTemplateId,
                userId = event.userId,
            )
        } catch (ex: Exception) {
            log.error("Redis 쿠폰 발급 선점 -> 확정 처리 중 오류가 발생했습니다. 이벤트: {}", event, ex)
        }
    }
}
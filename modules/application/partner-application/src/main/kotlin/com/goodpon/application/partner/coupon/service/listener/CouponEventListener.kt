package com.goodpon.application.partner.coupon.service.listener

import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CouponEventListener(
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    fun handleRedeemCouponRollbackEvent(event: RedeemCouponRollbackEvent) {
        log.error("쿠폰 사용에 실패하여 쿠폰 통계 캐시를 롤백합니다. 이벤트: {}", event)
        couponTemplateStatsCache.cancelRedeem(couponTemplateId = event.couponTemplateId, userId = event.userId)
    }
}
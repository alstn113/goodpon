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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    fun handleIssueCouponRollbackEvent(event: IssueCouponRollbackEvent) {
        log.error("쿠폰 발급에 실패하여 쿠폰 통계 캐시를 롤백합니다. 이벤트: {}", event)
        couponTemplateStatsCache.cancelIssue(couponTemplateId = event.couponTemplateId, userId = event.userId)
    }
}
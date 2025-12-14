package com.goodpon.partner.application.coupon.service.listener

import com.goodpon.partner.application.coupon.port.out.CouponIssueStore
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CouponEventListener(
    private val couponIssueStore: CouponIssueStore,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    fun handleRedeemCouponRollbackEvent(event: RedeemCouponRollbackEvent) {
        log.error("쿠폰 사용에 실패하여 쿠폰 통계 캐시를 롤백합니다. 이벤트: {}", event)
        couponIssueStore.cancelRedeem(couponTemplateId = event.couponTemplateId, userId = event.userId)
    }
}
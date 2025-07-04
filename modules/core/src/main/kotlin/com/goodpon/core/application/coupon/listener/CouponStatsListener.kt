package com.goodpon.core.application.coupon.listener

import com.goodpon.core.application.coupon.event.CouponIssuedEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CouponStatsListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleCouponIssuedEvent(event: CouponIssuedEvent) {

    }
}
package com.goodpon.dashboard.scheduler

import com.goodpon.dashboard.application.coupon.port.`in`.ReconcileStaleCouponIssueUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CouponIssueReconciliationScheduler(
    private val reconcileStaleCouponIssueUseCase: ReconcileStaleCouponIssueUseCase,
) {

    // 3분마다 3분 이상된 쿠폰 발급 예약 건들에 대해서 메세지 재발행
    @Scheduled(fixedDelay = 3 * 60 * 1000) // 3분마다
    fun reconcileStaleIssues() {
        reconcileStaleCouponIssueUseCase(olderThan = Duration.ofMinutes(3))
    }
}
package com.goodpon.api.dashboard.scheduler

import com.goodpon.application.dashboard.coupon.port.`in`.SyncCouponTemplateStatsUseCase
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CouponTemplateStatsBatchScheduler(
    private val syncCouponTemplateStatsUseCase: SyncCouponTemplateStatsUseCase,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 5 * 60 * 1000) // 5분마다
    fun syncCouponTemplateStats() {
        try {
            syncCouponTemplateStatsUseCase()
        } catch (e: Exception) {
            log.error("쿠폰 템플릿 통계 동기화 중 오류가 발생했습니다.", e)
        }
    }
}
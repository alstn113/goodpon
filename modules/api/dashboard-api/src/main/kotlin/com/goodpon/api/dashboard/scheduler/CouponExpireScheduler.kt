package com.goodpon.api.dashboard.scheduler

import com.goodpon.application.dashboard.coupon.service.ExpireCouponAndTemplateService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CouponExpireScheduler(
    private val expireCouponAndTemplateService: ExpireCouponAndTemplateService,
) {

    @Scheduled(cron = "0 0 0 * * *")
    fun expireExpiredCouponsAndTemplatesBatch() {
        expireCouponAndTemplateService(LocalDateTime.now())
    }
}
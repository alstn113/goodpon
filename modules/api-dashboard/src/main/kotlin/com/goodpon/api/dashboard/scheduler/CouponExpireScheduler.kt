package com.goodpon.api.dashboard.scheduler

import com.goodpon.core.application.coupon.CouponExpireBatchService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CouponExpireScheduler(
    private val couponExpireBatchService: CouponExpireBatchService,
) {

    @Scheduled(cron = "0 0 0 * * *")
    fun runExpireBatch() {
        couponExpireBatchService.expireCouponsAndTemplates(LocalDateTime.now())
    }
}
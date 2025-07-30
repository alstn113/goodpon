package com.goodpon.application.dashboard.coupon.service

import com.goodpon.application.dashboard.coupon.port.`in`.SyncCouponTemplateStatsUseCase
import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.dashboard.coupon.service.accessor.CouponTemplateStatsAccessor
import org.springframework.stereotype.Service

@Service
class SyncCouponTemplateStatsService(
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
    private val couponTemplateStatsAccessor: CouponTemplateStatsAccessor,
) : SyncCouponTemplateStatsUseCase {

    override fun invoke() {
        val statsMap = couponTemplateStatsCache.readAllStats()
        couponTemplateStatsAccessor.batchUpdateCouponTemplateStats(statsMap)
    }
}
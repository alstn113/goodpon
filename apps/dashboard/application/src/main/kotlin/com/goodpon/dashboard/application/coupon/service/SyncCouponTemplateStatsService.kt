package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.SyncCouponTemplateStatsUseCase
import com.goodpon.dashboard.application.coupon.port.out.CouponStatsStore
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateStatsAccessor
import org.springframework.stereotype.Service

@Service
class SyncCouponTemplateStatsService(
    private val couponStatsStore: CouponStatsStore,
    private val couponTemplateStatsAccessor: CouponTemplateStatsAccessor,
) : SyncCouponTemplateStatsUseCase {

    override fun invoke() {
        val statsMap = couponStatsStore.readAllStats()
        couponTemplateStatsAccessor.batchUpdateCouponTemplateStats(statsMap)
    }
}
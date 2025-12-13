package com.goodpon.application.dashboard.coupon.service

import com.goodpon.application.dashboard.coupon.port.`in`.ReconcileStaleCouponIssueUseCase
import com.goodpon.application.dashboard.coupon.port.out.CouponEventPublisher
import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.dashboard.coupon.port.out.dto.IssueCouponRequestedEvent
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class ReconcileStaleCouponIssueService(
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
    private val couponIssuePublisher: CouponEventPublisher,
) : ReconcileStaleCouponIssueUseCase {

    override fun invoke(olderThan: Duration) {
        val reservations = couponTemplateStatsCache.getAllStaleCouponIssueReservations(olderThan)
        for ((couponTemplateId, userIds) in reservations) {
            for (userId in userIds) {
                val event = IssueCouponRequestedEvent(
                    couponTemplateId = couponTemplateId,
                    userId = userId,
                )
                couponIssuePublisher.publishIssueCouponRequested(event)
            }
        }
    }
}
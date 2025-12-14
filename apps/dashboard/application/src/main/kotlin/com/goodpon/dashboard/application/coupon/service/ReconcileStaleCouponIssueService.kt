package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.ReconcileStaleCouponIssueUseCase
import com.goodpon.dashboard.application.coupon.port.out.CouponEventPublisher
import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.dashboard.application.coupon.port.out.dto.IssueCouponRequestedEvent
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
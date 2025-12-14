package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.ReconcileStaleCouponIssueUseCase
import com.goodpon.dashboard.application.coupon.port.out.CouponEventPublisher
import com.goodpon.dashboard.application.coupon.port.out.CouponStatsStore
import com.goodpon.dashboard.application.coupon.port.out.dto.IssueCouponRequestedEvent
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class ReconcileStaleCouponIssueService(
    private val couponStatsStore: CouponStatsStore,
    private val couponIssuePublisher: CouponEventPublisher,
) : ReconcileStaleCouponIssueUseCase {

    override fun invoke(olderThan: Duration) {
        val reservations = couponStatsStore.getAllStaleCouponIssueReservations(olderThan)
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
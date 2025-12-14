package com.goodpon.couponissuer.infra.store

import com.goodpon.couponissuer.application.port.out.CouponIssueStore
import com.goodpon.infra.redis.coupon.CouponIssueRedisStore
import org.springframework.stereotype.Component

@Component
class CouponIssueRedisStoreAdapter(
    private val couponIssueStore: CouponIssueRedisStore,
) : CouponIssueStore {

    override fun existsIssueReservation(couponTemplateId: Long, userId: String): Boolean {
        return couponIssueStore.existsReservation(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun completeIssueCoupon(couponTemplateId: Long, userId: String) {
        couponIssueStore.complete(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun markIssueReservationAsFailed(couponTemplateId: Long, userId: String) {
        couponIssueStore.markIssueReservationAsFailed(couponTemplateId = couponTemplateId, userId = userId)
    }

    override fun cancelIssue(couponTemplateId: Long, userId: String) {
        couponIssueStore.cancelIssue(couponTemplateId = couponTemplateId, userId = userId)
    }
}
package com.goodpon.core.domain.coupon.history

interface CouponHistoryRepository {

    fun save(couponHistory: CouponHistory): CouponHistory

    fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistory>
}
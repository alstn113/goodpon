package com.goodpon.domain.coupon.history

interface CouponHistoryRepository {

    fun save(couponHistory: CouponHistory): CouponHistory

    fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistory>
}
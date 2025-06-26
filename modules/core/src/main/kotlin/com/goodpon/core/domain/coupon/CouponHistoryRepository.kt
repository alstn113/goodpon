package com.goodpon.core.domain.coupon

interface CouponHistoryRepository {

    fun save(couponHistory: CouponHistory): CouponHistory
}
package com.goodpon.application.partner.coupon.port.out

import com.goodpon.domain.coupon.history.CouponHistory

interface CouponHistoryRepository {

    fun save(couponHistory: CouponHistory): CouponHistory

    fun findLastCouponHistory(userCouponId: String): CouponHistory?
}
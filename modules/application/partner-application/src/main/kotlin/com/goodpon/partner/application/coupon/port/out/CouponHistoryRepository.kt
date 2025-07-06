package com.goodpon.partner.application.coupon.port.out

import com.goodpon.domain.coupon.history.CouponHistory

interface CouponHistoryRepository {

    fun save(couponHistory: CouponHistory): CouponHistory

    fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistory>
}
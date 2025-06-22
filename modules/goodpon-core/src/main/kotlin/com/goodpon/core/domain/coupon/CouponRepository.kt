package com.goodpon.core.domain.coupon

interface CouponRepository {

    fun save(coupon: Coupon): Coupon
    fun findByAccountIdAndCouponTemplateId(accountId: Long, couponTemplateId: Long): Coupon?
}
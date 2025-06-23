package com.goodpon.core.domain.coupon

interface IssuedCouponRepository {

    fun save(issuedCoupon: IssuedCoupon): IssuedCoupon
    fun findByAccountIdAndCouponTemplateId(accountId: Long, couponTemplateId: Long): IssuedCoupon?
}
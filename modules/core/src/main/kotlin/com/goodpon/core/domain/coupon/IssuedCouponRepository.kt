package com.goodpon.core.domain.coupon

interface IssuedCouponRepository {

    fun save(issuedCoupon: IssuedCoupon): IssuedCoupon
    fun findByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): IssuedCoupon?
    fun findByIdForUpdate(id: String): IssuedCoupon?
}
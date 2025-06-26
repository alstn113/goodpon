package com.goodpon.core.domain.coupon

interface UserCouponRepository {

    fun save(issuedCoupon: UserCoupon): UserCoupon
    fun findByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): UserCoupon?
    fun findByIdForUpdate(id: String): UserCoupon?
}
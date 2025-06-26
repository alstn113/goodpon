package com.goodpon.core.domain.coupon

interface UserCouponRepository {

    fun save(userCoupon: UserCoupon): UserCoupon
    fun findByIdForUpdate(id: String): UserCoupon?
    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean
}
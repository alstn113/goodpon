package com.goodpon.domain.coupon.user

import java.time.LocalDateTime

interface UserCouponRepository {

    fun save(userCoupon: com.goodpon.domain.coupon.user.UserCoupon): com.goodpon.domain.coupon.user.UserCoupon

    fun saveAll(userCoupons: List<com.goodpon.domain.coupon.user.UserCoupon>): List<com.goodpon.domain.coupon.user.UserCoupon>

    fun findByIdForUpdate(id: String): com.goodpon.domain.coupon.user.UserCoupon?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findByStatusAndExpiresAtLessThanEqual(status: UserCouponStatus, expiresAt: LocalDateTime): List<com.goodpon.domain.coupon.user.UserCoupon>
}
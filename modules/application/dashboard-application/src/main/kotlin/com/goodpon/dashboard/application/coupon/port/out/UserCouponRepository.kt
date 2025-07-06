package com.goodpon.dashboard.application.coupon.port.out

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import java.time.LocalDateTime

interface UserCouponRepository {

    fun save(userCoupon: UserCoupon): UserCoupon

    fun saveAll(userCoupons: List<UserCoupon>): List<UserCoupon>

    fun findByIdForUpdate(id: String): UserCoupon?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findByStatusAndExpiresAtLessThanEqual(status: UserCouponStatus, expiresAt: LocalDateTime): List<UserCoupon>
}
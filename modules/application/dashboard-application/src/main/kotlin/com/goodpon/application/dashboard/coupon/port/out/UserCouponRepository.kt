package com.goodpon.application.dashboard.coupon.port.out

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import java.time.LocalDateTime

interface UserCouponRepository {

    fun saveAll(userCoupons: List<UserCoupon>): List<UserCoupon>

    fun findByStatusAndExpiresAtLessThanEqual(status: UserCouponStatus, expiresAt: LocalDateTime): List<UserCoupon>
}
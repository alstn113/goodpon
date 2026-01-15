package com.goodpon.dashboard.application.coupon.port.out

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import java.time.LocalDateTime

interface UserCouponRepository {

    fun saveAll(userCoupons: List<UserCoupon>)

    fun findByStatusAndExpiresAtLessThanEqual(status: UserCouponStatus, expiresAt: LocalDateTime): List<UserCoupon>
}
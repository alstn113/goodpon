package com.goodpon.couponissuer.application.port.out

import com.goodpon.domain.coupon.user.UserCoupon

interface UserCouponRepository {

    fun save(userCoupon: UserCoupon): UserCoupon
    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean
}
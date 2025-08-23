package com.goodpon.application.couponissuer.port.out

import com.goodpon.domain.coupon.user.UserCoupon

interface UserCouponRepository {

    fun save(userCoupon: UserCoupon): UserCoupon
    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean
}
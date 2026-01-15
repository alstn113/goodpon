package com.goodpon.couponissuer.application.port.out

import com.goodpon.domain.coupon.user.UserCoupon

interface UserCouponRepository {

    fun save(userCoupon: UserCoupon): UserCoupon

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findAllByUserIdInAndCouponTemplateIdIn(userIds: List<String>, couponTemplateIds: List<Long>): List<UserCoupon>

    fun saveAll(userCoupons: List<UserCoupon>)
}
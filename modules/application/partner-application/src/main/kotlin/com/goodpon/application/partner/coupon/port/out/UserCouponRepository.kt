package com.goodpon.application.partner.coupon.port.out

import com.goodpon.application.partner.coupon.service.dto.AvailableUserCouponView
import com.goodpon.application.partner.coupon.service.dto.UserCouponView
import com.goodpon.domain.coupon.user.UserCoupon

interface UserCouponRepository {

    fun save(userCoupon: UserCoupon): UserCoupon

    fun findByIdForUpdate(id: String): UserCoupon?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findUserCouponsView(userId: String, merchantId: Long): List<UserCouponView>

    fun findAvailableUserCouponsView(userId: String, merchantId: Long, orderAmount: Int): List<AvailableUserCouponView>
}
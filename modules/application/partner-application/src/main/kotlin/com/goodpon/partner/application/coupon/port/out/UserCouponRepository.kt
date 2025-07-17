package com.goodpon.partner.application.coupon.port.out

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponView
import com.goodpon.partner.application.coupon.service.dto.UserCouponView

interface UserCouponRepository {

    fun save(userCoupon: UserCoupon): UserCoupon

    fun findByIdForUpdate(id: String): UserCoupon?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findUserCouponsView(userId: String, merchantId: Long): List<UserCouponView>

    fun findAvailableUserCouponsView(userId: String, merchantId: Long, orderAmount: Int): List<AvailableUserCouponView>
}
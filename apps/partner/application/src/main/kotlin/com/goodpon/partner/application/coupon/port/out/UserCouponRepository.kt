package com.goodpon.partner.application.coupon.port.out

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponView
import com.goodpon.partner.application.coupon.service.dto.UserCouponSummary

interface UserCouponRepository {

    fun save(userCoupon: UserCoupon): UserCoupon

    fun findByIdForUpdate(id: String): UserCoupon?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findUserCouponSummaries(userId: String, merchantId: Long): List<UserCouponSummary>

    fun findAvailableUserCouponsView(userId: String, merchantId: Long, orderAmount: Int): List<AvailableUserCouponView>
}
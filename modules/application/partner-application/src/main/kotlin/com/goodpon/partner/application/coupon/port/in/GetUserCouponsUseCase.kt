package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.service.dto.UserCouponsView

interface GetUserCouponsUseCase {

    fun getUserCoupons(merchantId: Long, userId: String): UserCouponsView
}
package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponsView

interface GetAvailableUserCouponsUseCase {

    fun getAvailableUserCoupons(merchantId: Long, userId: String, orderAmount: Int): AvailableUserCouponsView
}
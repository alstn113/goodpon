package com.goodpon.application.partner.coupon.port.`in`

import com.goodpon.application.partner.coupon.service.dto.UserCouponList

fun interface GetUserCouponsUseCase {

    operator fun invoke(merchantId: Long, userId: String): UserCouponList
}
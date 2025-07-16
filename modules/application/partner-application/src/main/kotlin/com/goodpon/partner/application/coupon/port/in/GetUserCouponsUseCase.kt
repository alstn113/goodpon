package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.service.dto.UserCouponsView

fun interface GetUserCouponsUseCase {

    operator fun invoke(merchantId: Long, userId: String): UserCouponsView
}
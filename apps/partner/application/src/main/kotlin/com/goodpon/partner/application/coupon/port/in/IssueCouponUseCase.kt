package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand

fun interface IssueCouponUseCase {

    operator fun invoke(command: IssueCouponCommand)
}
package com.goodpon.application.partner.coupon.port.`in`

import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand

fun interface IssueCouponUseCase {

    operator fun invoke(command: IssueCouponCommand)
}
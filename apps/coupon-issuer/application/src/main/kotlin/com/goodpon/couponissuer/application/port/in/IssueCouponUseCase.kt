package com.goodpon.couponissuer.application.port.`in`

import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand

fun interface IssueCouponUseCase {

    operator fun invoke(command: IssueCouponCommand)
}
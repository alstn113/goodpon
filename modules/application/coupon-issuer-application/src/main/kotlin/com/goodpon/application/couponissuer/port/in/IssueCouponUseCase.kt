package com.goodpon.application.couponissuer.port.`in`

import com.goodpon.application.couponissuer.port.`in`.dto.IssueCouponCommand

fun interface IssueCouponUseCase {

    operator fun invoke(command: IssueCouponCommand)
}
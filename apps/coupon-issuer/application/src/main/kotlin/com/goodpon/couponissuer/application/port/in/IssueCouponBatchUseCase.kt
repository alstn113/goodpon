package com.goodpon.couponissuer.application.port.`in`

import com.goodpon.couponissuer.application.port.`in`.dto.IssueCouponCommand

fun interface IssueCouponBatchUseCase {

    operator fun invoke(commands: List<IssueCouponCommand>)
}
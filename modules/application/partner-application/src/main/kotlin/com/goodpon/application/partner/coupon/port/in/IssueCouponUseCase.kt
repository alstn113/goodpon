package com.goodpon.application.partner.coupon.port.`in`

import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponResult

fun interface IssueCouponUseCase {

    operator fun invoke(command: IssueCouponCommand): IssueCouponResult
}
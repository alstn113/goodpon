package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponResult

fun interface RedeemCouponUseCase {

    operator fun invoke(command: RedeemCouponCommand): RedeemCouponResult
}
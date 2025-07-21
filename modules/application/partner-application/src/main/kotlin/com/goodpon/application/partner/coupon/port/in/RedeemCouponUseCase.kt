package com.goodpon.application.partner.coupon.port.`in`

import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponResult

fun interface RedeemCouponUseCase {

    operator fun invoke(command: RedeemCouponCommand): RedeemCouponResult
}
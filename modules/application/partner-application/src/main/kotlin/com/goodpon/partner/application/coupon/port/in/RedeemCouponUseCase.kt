package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponResult

interface RedeemCouponUseCase {

    fun redeemCoupon(command: RedeemCouponCommand): RedeemCouponResult
}
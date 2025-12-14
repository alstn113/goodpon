package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionResult

fun interface CancelCouponRedemptionUseCase {

    operator fun invoke(command: CancelCouponRedemptionCommand): CancelCouponRedemptionResult
}
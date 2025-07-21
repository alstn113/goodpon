package com.goodpon.application.partner.coupon.port.`in`

import com.goodpon.application.partner.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.CancelCouponRedemptionResult

fun interface CancelCouponRedemptionUseCase {

    operator fun invoke(command: CancelCouponRedemptionCommand): CancelCouponRedemptionResult
}
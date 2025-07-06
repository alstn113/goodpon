package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionResult

interface CancelCouponRedemptionUseCase {

    fun cancelCouponRedemption(command: CancelCouponRedemptionCommand): CancelCouponRedemptionResult
}
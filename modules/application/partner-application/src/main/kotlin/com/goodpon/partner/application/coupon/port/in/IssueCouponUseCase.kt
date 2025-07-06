package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponResult

interface IssueCouponUseCase {

    fun issueCoupon(command: IssueCouponCommand): IssueCouponResult
}
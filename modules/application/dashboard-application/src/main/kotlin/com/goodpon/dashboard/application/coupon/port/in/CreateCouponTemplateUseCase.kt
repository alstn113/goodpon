package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult

fun interface CreateCouponTemplateUseCase {

    operator fun invoke(command: CreateCouponTemplateCommand): CreateCouponTemplateResult
}
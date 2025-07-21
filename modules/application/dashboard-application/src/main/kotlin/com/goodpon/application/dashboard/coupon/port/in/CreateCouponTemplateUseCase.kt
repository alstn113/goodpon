package com.goodpon.application.dashboard.coupon.port.`in`

import com.goodpon.application.dashboard.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.application.dashboard.coupon.port.`in`.dto.CreateCouponTemplateResult

fun interface CreateCouponTemplateUseCase {

    operator fun invoke(command: CreateCouponTemplateCommand): CreateCouponTemplateResult
}
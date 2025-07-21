package com.goodpon.application.dashboard.coupon.port.`in`

import com.goodpon.application.dashboard.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.application.dashboard.coupon.port.`in`.dto.PublishCouponTemplateResult

fun interface PublishCouponTemplateUseCase {

    operator fun invoke(command: PublishCouponTemplateCommand): PublishCouponTemplateResult
}
package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult

fun interface PublishCouponTemplateUseCase {

    operator fun invoke(command: PublishCouponTemplateCommand): PublishCouponTemplateResult
}
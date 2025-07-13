package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult

interface PublishCouponTemplateUseCase {

    fun publishCouponTemplate(command: PublishCouponTemplateCommand): PublishCouponTemplateResult
}
package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult

interface CreateCouponTemplateUseCase {

    fun createCouponTemplate(command: CreateCouponTemplateCommand): CreateCouponTemplateResult
}
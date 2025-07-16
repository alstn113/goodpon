package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.service.dto.CouponTemplateStatusesView

interface GetCouponTemplateStatusesUseCase {

    fun getCouponTemplateStatuses(
        merchantId: Long,
        couponTemplateId: Long,
        userId: String? = null,
    ): CouponTemplateStatusesView
}
package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummaries

interface GetMerchantCouponTemplatesUseCase {

    fun getMerchantCouponTemplates(accountId: Long, merchantId: Long): CouponTemplateSummaries
}
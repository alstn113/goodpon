package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary

interface GetMyCouponTemplatesUseCase {

    fun getMerchantCouponTemplates(accountId: Long, merchantId: Long): List<CouponTemplateSummary>
}
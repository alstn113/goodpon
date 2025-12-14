package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummaries

fun interface GetMerchantCouponTemplatesUseCase {

    operator fun invoke(accountId: Long, merchantId: Long): CouponTemplateSummaries
}
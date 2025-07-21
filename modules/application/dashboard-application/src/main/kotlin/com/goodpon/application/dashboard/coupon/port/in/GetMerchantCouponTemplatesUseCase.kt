package com.goodpon.application.dashboard.coupon.port.`in`

import com.goodpon.application.dashboard.coupon.service.dto.CouponTemplateSummaries

fun interface GetMerchantCouponTemplatesUseCase {

    operator fun invoke(accountId: Long, merchantId: Long): CouponTemplateSummaries
}
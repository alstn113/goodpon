package com.goodpon.application.dashboard.coupon.port.`in`

import com.goodpon.application.dashboard.coupon.port.`in`.dto.GetMerchantCouponTemplateDetailQuery
import com.goodpon.application.dashboard.coupon.service.dto.CouponTemplateDetail

fun interface GetMerchantCouponTemplateDetailUseCase {

    operator fun invoke(query: GetMerchantCouponTemplateDetailQuery): CouponTemplateDetail
}
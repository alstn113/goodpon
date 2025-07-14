package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.port.`in`.dto.GetMerchantCouponTemplateDetailQuery
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail

interface GetMerchantCouponTemplateDetailUseCase {

    fun getMerchantCouponTemplateDetail(query: GetMerchantCouponTemplateDetailQuery): CouponTemplateDetail
}
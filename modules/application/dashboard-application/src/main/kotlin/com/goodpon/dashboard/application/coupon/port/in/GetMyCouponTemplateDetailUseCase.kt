package com.goodpon.dashboard.application.coupon.port.`in`

import com.goodpon.dashboard.application.coupon.port.`in`.dto.GetMerchantCouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail

interface GetMyCouponTemplateDetailUseCase {

    fun getMerchantCouponTemplateDetail(query: GetMerchantCouponTemplateDetail): CouponTemplateDetail
}
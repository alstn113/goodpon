package com.goodpon.application.dashboard.coupon.port.`in`.dto

import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus

data class PublishCouponTemplateResult(
    val id: Long,
    val name: String,
    val merchantId: Long,
    val status: CouponTemplateStatus,
)
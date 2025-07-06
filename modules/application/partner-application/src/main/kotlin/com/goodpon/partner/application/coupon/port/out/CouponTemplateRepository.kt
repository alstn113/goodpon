package com.goodpon.partner.application.coupon.port.out

import com.goodpon.domain.coupon.template.CouponTemplate

interface CouponTemplateRepository {

    fun findById(id: Long): CouponTemplate?
}
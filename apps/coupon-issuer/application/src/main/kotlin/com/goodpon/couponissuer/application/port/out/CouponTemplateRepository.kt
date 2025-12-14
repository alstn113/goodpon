package com.goodpon.couponissuer.application.port.out

import com.goodpon.domain.coupon.template.CouponTemplate

interface CouponTemplateRepository {

    fun findById(couponTemplateId: Long): CouponTemplate?
}
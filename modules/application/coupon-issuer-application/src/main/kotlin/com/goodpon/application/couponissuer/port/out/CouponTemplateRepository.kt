package com.goodpon.application.couponissuer.port.out

import com.goodpon.domain.coupon.template.CouponTemplate

interface CouponTemplateRepository {

    fun findById(couponTemplateId: Long): CouponTemplate?
}
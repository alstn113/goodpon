package com.goodpon.partner.application.coupon.port.out

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.partner.application.coupon.service.dto.CouponTemplateDetail

interface CouponTemplateRepository {

    fun findById(id: Long): CouponTemplate?

    fun findCouponTemplateDetail(couponTemplateId: Long, merchantId: Long): CouponTemplateDetail?
}
package com.goodpon.application.partner.coupon.port.out

import com.goodpon.application.partner.coupon.service.dto.CouponTemplateDetail
import com.goodpon.domain.coupon.template.CouponTemplate

interface CouponTemplateRepository {

    fun findById(id: Long): CouponTemplate?

    fun findCouponTemplateDetail(couponTemplateId: Long, merchantId: Long): CouponTemplateDetail?
}
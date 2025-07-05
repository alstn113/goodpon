package com.goodpon.domain.coupon.template

import com.goodpon.domain.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

interface CouponTemplateRepository {

    fun save(couponTemplate: com.goodpon.domain.coupon.template.CouponTemplate): com.goodpon.domain.coupon.template.CouponTemplate

    fun saveAll(couponTemplates: List<com.goodpon.domain.coupon.template.CouponTemplate>): List<com.goodpon.domain.coupon.template.CouponTemplate>

    fun findById(id: Long): com.goodpon.domain.coupon.template.CouponTemplate?

    fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<com.goodpon.domain.coupon.template.CouponTemplate>
}
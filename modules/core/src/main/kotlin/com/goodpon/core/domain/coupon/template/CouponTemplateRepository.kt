package com.goodpon.core.domain.coupon.template

import com.goodpon.core.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

interface CouponTemplateRepository {
    fun save(couponTemplate: CouponTemplate): CouponTemplate
    fun saveAll(couponTemplates: List<CouponTemplate>): List<CouponTemplate>
    fun findById(id: Long): CouponTemplate?
    fun findByIdForRead(id: Long): CouponTemplate?
    fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplate>
}
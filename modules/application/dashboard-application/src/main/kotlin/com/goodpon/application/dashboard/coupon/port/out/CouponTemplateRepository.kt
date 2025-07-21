package com.goodpon.application.dashboard.coupon.port.out

import com.goodpon.application.dashboard.coupon.service.dto.CouponTemplateDetail
import com.goodpon.application.dashboard.coupon.service.dto.CouponTemplateSummary
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import java.time.LocalDateTime

interface CouponTemplateRepository {

    fun save(couponTemplate: CouponTemplate): CouponTemplate

    fun saveAll(couponTemplates: List<CouponTemplate>): List<CouponTemplate>

    fun findById(couponTemplateId: Long): CouponTemplate?

    fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplate>

    fun findCouponTemplateSummaries(merchantId: Long): List<CouponTemplateSummary>

    fun findCouponTemplateDetail(couponTemplateId: Long): CouponTemplateDetail?
}
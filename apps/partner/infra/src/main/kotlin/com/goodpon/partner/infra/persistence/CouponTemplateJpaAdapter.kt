package com.goodpon.partner.infra.persistence

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.infra.jpa.core.CouponTemplateCoreRepository
import com.goodpon.partner.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.partner.application.coupon.service.dto.CouponTemplateDetail
import org.springframework.stereotype.Repository

@Repository
class CouponTemplateJpaAdapter(
    private val couponTemplateCoreRepository: CouponTemplateCoreRepository,
) : CouponTemplateRepository {

    override fun findById(id: Long): CouponTemplate? {
        return couponTemplateCoreRepository.findById(id)
    }

    override fun findCouponTemplateDetail(couponTemplateId: Long, merchantId: Long): CouponTemplateDetail? {
        val dto = couponTemplateCoreRepository.findDetailById(
            couponTemplateId = couponTemplateId,
            merchantId = merchantId
        ) ?: return null

        return CouponTemplateDetail(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            minOrderAmount = dto.minOrderAmount,
            discountType = dto.discountType,
            discountValue = dto.discountValue,
            maxDiscountAmount = dto.maxDiscountAmount,
            status = dto.status,
            issueStartAt = dto.issueStartAt,
            issueEndAt = dto.issueEndAt,
            validityDays = dto.validityDays,
            absoluteExpiresAt = dto.absoluteExpiresAt,
            limitType = dto.limitType,
            maxIssueCount = dto.maxIssueCount,
            maxRedeemCount = dto.maxRedeemCount,
        )
    }
}

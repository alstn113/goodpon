package com.goodpon.infra.db.jpa.adapter.partner

import com.goodpon.application.partner.coupon.port.out.CouponTemplateRepository
import com.goodpon.application.partner.coupon.service.dto.CouponTemplateDetail
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.infra.db.jpa.core.CouponTemplateCoreRepository
import org.springframework.stereotype.Repository

@Repository("partnerCouponTemplateJpaAdapter")
class CouponTemplateJpaAdapter(
    private val couponTemplateCoreRepository: CouponTemplateCoreRepository,
) : CouponTemplateRepository {

    override fun findById(id: Long): CouponTemplate? {
        return couponTemplateCoreRepository.findById(id)
    }

    override fun findCouponTemplateDetail(couponTemplateId: Long, merchantId: Long): CouponTemplateDetail? {
        val dto = couponTemplateCoreRepository.findByIdAndMerchantIdWithStats(
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
            issueCount = dto.issueCount,
            redeemCount = dto.redeemCount,
        )
    }
}

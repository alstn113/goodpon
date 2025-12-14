package com.goodpon.dashboard.infra.persistence

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.jpa.core.CouponTemplateCoreRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CouponTemplateJpaAdapter(
    private val couponTemplateCoreRepository: CouponTemplateCoreRepository,
) : CouponTemplateRepository {

    override fun save(couponTemplate: CouponTemplate): CouponTemplate {
        return try {
            couponTemplateCoreRepository.save(couponTemplate)
        } catch (e: EntityNotFoundException) {
            throw CouponTemplateNotFoundException()
        }
    }

    override fun saveAll(couponTemplates: List<CouponTemplate>): List<CouponTemplate> {
        return couponTemplateCoreRepository.saveAll(couponTemplates)
    }

    override fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplate> {
        return couponTemplateCoreRepository.findByStatusAndAbsoluteExpiresAtLessThanEqual(
            status,
            absoluteExpiresAt
        )
    }

    override fun findById(couponTemplateId: Long): CouponTemplate? {
        return couponTemplateCoreRepository.findById(couponTemplateId)
    }

    override fun findCouponTemplateSummaries(merchantId: Long): List<CouponTemplateSummary> {
        return couponTemplateCoreRepository.findCouponTemplateSummaries(merchantId)
            .map {
                CouponTemplateSummary(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    status = it.status,
                    issueCount = it.issueCount,
                    redeemCount = it.redeemCount,
                    createdAt = it.createdAt
                )
            }
    }

    override fun findCouponTemplateDetail(couponTemplateId: Long): CouponTemplateDetail? {
        val dto = couponTemplateCoreRepository.findByIdWithStats(couponTemplateId)
            ?: return null

        return CouponTemplateDetail(
            id = dto.id,
            merchantId = dto.merchantId,
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
            createdAt = dto.createdAt
        )
    }

}
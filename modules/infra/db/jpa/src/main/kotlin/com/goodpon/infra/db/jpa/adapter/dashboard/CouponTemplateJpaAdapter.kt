package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.db.jpa.core.CouponTemplateCoreRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository("dashboardCouponTemplateJpaAdapter")
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
                    issuedCount = it.issuedCount,
                    redeemedCount = it.redeemedCount,
                    createdAt = it.createdAt
                )
            }
    }

    override fun findCouponTemplateDetail(couponTemplateId: Long): CouponTemplateDetail? {
        val couponTemplateDetail = couponTemplateCoreRepository.findCouponTemplateDetail(couponTemplateId)
            ?: return null

        return CouponTemplateDetail(
            id = couponTemplateDetail.id,
            merchantId = couponTemplateDetail.merchantId,
            name = couponTemplateDetail.name,
            description = couponTemplateDetail.description,
            minOrderAmount = couponTemplateDetail.minOrderAmount,
            discountType = couponTemplateDetail.discountType,
            discountValue = couponTemplateDetail.discountValue,
            maxDiscountAmount = couponTemplateDetail.maxDiscountAmount,
            status = couponTemplateDetail.status,
            issueStartAt = couponTemplateDetail.issueStartAt,
            issueEndAt = couponTemplateDetail.issueEndAt,
            validityDays = couponTemplateDetail.validityDays,
            absoluteExpiresAt = couponTemplateDetail.absoluteExpiresAt,
            limitType = couponTemplateDetail.limitType,
            maxIssueCount = couponTemplateDetail.maxIssueCount,
            maxRedeemCount = couponTemplateDetail.maxRedeemCount,
            issueCount = couponTemplateDetail.issueCount,
            redeemCount = couponTemplateDetail.redeemCount,
            createdAt = couponTemplateDetail.createdAt
        )
    }

}
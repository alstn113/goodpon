package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
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
}
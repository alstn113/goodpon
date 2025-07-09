package com.goodpon.infra.jpa.adapter.dashboard

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.jpa.core.CouponTemplateCoreRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CouponTemplateJpaAdapter(
    private val couponTemplateCoreRepository: CouponTemplateCoreRepository,
) : CouponTemplateRepository {

    override fun save(couponTemplate: CouponTemplate): CouponTemplate {
        return couponTemplateCoreRepository.save(couponTemplate)
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
}
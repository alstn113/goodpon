package com.goodpon.dashboard.application.coupon.service.accessor

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.domain.coupon.template.CouponTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStore(
    private val couponTemplateRepository: CouponTemplateRepository,
) {

    @Transactional
    fun create(couponTemplate: CouponTemplate): CouponTemplate {
        return couponTemplateRepository.save(couponTemplate)
    }

    @Transactional
    fun saveAll(couponTemplates: List<CouponTemplate>): List<CouponTemplate> {
        return couponTemplateRepository.saveAll(couponTemplates)
    }
}
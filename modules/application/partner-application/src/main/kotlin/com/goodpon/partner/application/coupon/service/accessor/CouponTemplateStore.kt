package com.goodpon.partner.application.coupon.service.accessor

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.CouponTemplateRepository
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
}
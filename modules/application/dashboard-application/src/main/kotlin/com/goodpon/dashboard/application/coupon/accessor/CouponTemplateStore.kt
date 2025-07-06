package com.goodpon.dashboard.application.coupon.accessor

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStore(
    private val couponTemplateRepository: com.goodpon.domain.coupon.template.CouponTemplateRepository,
) {

    @Transactional
    fun create(couponTemplate: com.goodpon.domain.coupon.template.CouponTemplate): com.goodpon.domain.coupon.template.CouponTemplate {
        return couponTemplateRepository.save(couponTemplate)
    }
}
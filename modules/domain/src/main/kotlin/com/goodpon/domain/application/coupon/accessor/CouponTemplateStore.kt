package com.goodpon.domain.application.coupon.accessor

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.CouponTemplateRepository
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
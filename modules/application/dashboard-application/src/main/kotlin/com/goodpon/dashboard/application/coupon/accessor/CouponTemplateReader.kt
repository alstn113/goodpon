package com.goodpon.dashboard.application.coupon.accessor

import com.goodpon.domain.coupon.template.exception.CouponTemplateNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateReader(
    private val couponTemplateRepository: com.goodpon.domain.coupon.template.CouponTemplateRepository,
) {

    @Transactional(readOnly = true)
    fun readById(couponTemplateId: Long): com.goodpon.domain.coupon.template.CouponTemplate {
        return couponTemplateRepository.findById(couponTemplateId)
            ?: throw CouponTemplateNotFoundException()
    }
}
package com.goodpon.core.application.coupon.accessor

import com.goodpon.core.application.coupon.exception.CouponTemplateNotFoundException
import com.goodpon.core.domain.coupon.template.CouponTemplate
import com.goodpon.core.domain.coupon.template.CouponTemplateRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateReader(
    private val couponTemplateRepository: CouponTemplateRepository,
) {
    @Transactional(readOnly = true)
    fun readByIdForRead(couponTemplateId: Long): CouponTemplate {
        return couponTemplateRepository.findByIdForRead(couponTemplateId)
            ?: throw CouponTemplateNotFoundException()
    }
}
package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.CouponTemplate
import com.goodpon.core.domain.coupon.CouponTemplateRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateReader(
    private val couponTemplateRepository: CouponTemplateRepository,
) {
    @Transactional(readOnly = true)
    fun readByIdForRead(couponTemplateId: Long): CouponTemplate {
        return couponTemplateRepository.findByIdForRead(couponTemplateId)
            ?: throw IllegalArgumentException("CouponTemplate with id $couponTemplateId not found")
    }
}
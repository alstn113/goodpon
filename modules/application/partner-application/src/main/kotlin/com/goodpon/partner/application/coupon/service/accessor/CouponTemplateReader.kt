package com.goodpon.partner.application.coupon.service.accessor

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.port.out.CouponTemplateRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateReader(
    private val couponTemplateRepository: CouponTemplateRepository,
) {

    @Transactional(readOnly = true)
    fun readById(couponTemplateId: Long): CouponTemplate {
        return couponTemplateRepository.findById(couponTemplateId)
            ?: throw CouponTemplateNotFoundException()
    }
}
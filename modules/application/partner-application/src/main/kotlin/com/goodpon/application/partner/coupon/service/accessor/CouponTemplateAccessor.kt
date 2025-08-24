package com.goodpon.application.partner.coupon.service.accessor

import com.goodpon.application.partner.coupon.port.out.CouponTemplateRepository
import com.goodpon.application.partner.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.domain.coupon.template.CouponTemplate
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateAccessor(
    private val couponTemplateRepository: CouponTemplateRepository,
) {

    @Transactional(readOnly = true)
    @Cacheable(value = ["couponTemplates:byId"], key = "#couponTemplateId")
    fun readById(couponTemplateId: Long): CouponTemplate {
        return couponTemplateRepository.findById(couponTemplateId)
            ?: throw CouponTemplateNotFoundException()
    }
}
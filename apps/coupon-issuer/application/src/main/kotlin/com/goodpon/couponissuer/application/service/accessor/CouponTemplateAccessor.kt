package com.goodpon.couponissuer.application.service.accessor

import com.goodpon.couponissuer.application.port.out.CouponTemplateRepository
import com.goodpon.couponissuer.application.service.exception.CouponTemplateNotFoundException
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

    @Transactional(readOnly = true)
    fun readAllByIdIn(couponTemplateIds: List<Long>): List<CouponTemplate> {
        return couponTemplateRepository.findAllByIdIn(couponTemplateIds)
    }
}
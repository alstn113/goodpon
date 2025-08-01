package com.goodpon.application.dashboard.coupon.service.accessor

import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateRepository
import com.goodpon.application.dashboard.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponTemplateAccessor(
    private val couponTemplateRepository: CouponTemplateRepository,
) {

    @Transactional(readOnly = true)
    fun readByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplate> {
        return couponTemplateRepository.findByStatusAndAbsoluteExpiresAtLessThanEqual(
            status = status,
            absoluteExpiresAt = absoluteExpiresAt
        )
    }

    @Transactional(readOnly = true)
    fun readById(couponTemplateId: Long): CouponTemplate {
        return couponTemplateRepository.findById(couponTemplateId)
            ?: throw CouponTemplateNotFoundException()
    }

    @Transactional
    fun save(couponTemplate: CouponTemplate): CouponTemplate {
        return couponTemplateRepository.save(couponTemplate)
    }

    @Transactional
    fun saveAll(couponTemplates: List<CouponTemplate>): List<CouponTemplate> {
        return couponTemplateRepository.saveAll(couponTemplates)
    }
}
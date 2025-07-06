package com.goodpon.dashboard.application.coupon.service.accessor

import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponTemplateReader(
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
}
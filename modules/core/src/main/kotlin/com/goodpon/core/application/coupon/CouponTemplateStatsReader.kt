package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.CouponTemplateStats
import com.goodpon.core.domain.coupon.CouponTemplateStatsRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsReader(
    private val couponTemplateStatsRepository: CouponTemplateStatsRepository,
) {
    @Transactional(readOnly = true)
    fun readByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats {
        return couponTemplateStatsRepository.findByCouponTemplateIdForUpdate(couponTemplateId)
            ?: throw IllegalArgumentException("쿠폰 템플릿 통계가 존재하지 않습니다.")
    }
}
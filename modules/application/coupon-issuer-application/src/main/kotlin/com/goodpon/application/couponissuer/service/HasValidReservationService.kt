package com.goodpon.application.couponissuer.service

import com.goodpon.application.couponissuer.port.`in`.HasValidReservationUseCase
import com.goodpon.application.couponissuer.port.out.CouponTemplateStatsCache
import org.springframework.stereotype.Service

@Service
class HasValidReservationService(
    private val couponTemplateStatsCache: CouponTemplateStatsCache
) : HasValidReservationUseCase {

    override fun invoke(couponTemplateId: Long, userId: String): Boolean {
        return couponTemplateStatsCache.hasValidReservation(couponTemplateId, userId)
    }
}
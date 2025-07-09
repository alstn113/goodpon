package com.goodpon.infra.jpa.adapter.dashboard

import com.goodpon.dashboard.application.coupon.port.out.CouponHistoryRepository
import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.jpa.core.CouponHistoryCoreRepository
import org.springframework.stereotype.Repository

@Repository
class CouponHistoryJpaAdapter(
    private val couponHistoryCoreRepository: CouponHistoryCoreRepository,
) : CouponHistoryRepository {

    override fun save(couponHistory: CouponHistory): CouponHistory {
        return couponHistoryCoreRepository.save(couponHistory)
    }
}

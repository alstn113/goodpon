package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.coupon.port.out.CouponHistoryRepository
import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.db.jpa.core.CouponHistoryCoreRepository
import org.springframework.stereotype.Repository

@Repository("dashboardCouponHistoryJpaAdapter")
class CouponHistoryJpaAdapter(
    private val couponHistoryCoreRepository: CouponHistoryCoreRepository,
) : CouponHistoryRepository {

    override fun save(couponHistory: CouponHistory): CouponHistory {
        return couponHistoryCoreRepository.save(couponHistory)
    }
}

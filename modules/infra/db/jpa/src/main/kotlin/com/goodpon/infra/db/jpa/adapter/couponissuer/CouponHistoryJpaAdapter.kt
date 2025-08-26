package com.goodpon.infra.db.jpa.adapter.couponissuer

import com.goodpon.application.couponissuer.port.out.CouponHistoryRepository
import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.db.jpa.core.CouponHistoryCoreRepository
import org.springframework.stereotype.Repository

@Repository("couponIssuerCouponHistoryJpaAdapter")
class CouponHistoryJpaAdapter(
    private val couponHistoryCoreRepository: CouponHistoryCoreRepository,
) : CouponHistoryRepository {

    override fun save(couponHistory: CouponHistory): CouponHistory {
        return couponHistoryCoreRepository.save(couponHistory)
    }
}
package com.goodpon.couponissuer.infra.persistence

import com.goodpon.couponissuer.application.port.out.CouponHistoryRepository
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
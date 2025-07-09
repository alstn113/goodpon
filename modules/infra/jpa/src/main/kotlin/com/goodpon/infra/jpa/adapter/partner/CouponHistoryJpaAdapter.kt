package com.goodpon.infra.jpa.adapter.partner

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.jpa.core.CouponHistoryCoreRepository
import com.goodpon.partner.application.coupon.port.out.CouponHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class CouponHistoryJpaAdapter(
    private val couponHistoryCoreRepository: CouponHistoryCoreRepository,
) : CouponHistoryRepository {

    override fun save(couponHistory: CouponHistory): CouponHistory {
        return couponHistoryCoreRepository.save(couponHistory)
    }

    override fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistory> {
        return couponHistoryCoreRepository.findByUserCouponIdOrderByRecordedAtDesc(userCouponId)
    }
}

package com.goodpon.partner.infra.persistence

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

    override fun findLastCouponHistory(userCouponId: String): CouponHistory? {
        return couponHistoryCoreRepository.findFirstByUserCouponIdOrderByRecordedAtDesc(userCouponId)
    }
}

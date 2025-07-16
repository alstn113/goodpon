package com.goodpon.infra.db.jpa.adapter.partner

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.db.jpa.core.CouponHistoryCoreRepository
import com.goodpon.partner.application.coupon.port.out.CouponHistoryRepository
import org.springframework.stereotype.Repository

@Repository("partnerCouponHistoryJpaAdapter")
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

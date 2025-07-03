package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.CouponHistory
import com.goodpon.core.domain.coupon.CouponHistoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponHistoryCoreRepository(
    private val couponHistoryJpaRepository: CouponHistoryJpaRepository,
) : CouponHistoryRepository {
    override fun save(couponHistory: CouponHistory): CouponHistory {
        val entity = couponHistoryJpaRepository.findByIdOrNull(couponHistory.id)
        if (entity == null) {
            val newEntity = CouponHistoryEntity.fromDomain(couponHistory)
            val savedEntity = couponHistoryJpaRepository.save(newEntity)
            return savedEntity.toDomain()
        }

        entity.update(couponHistory)
        val savedEntity = couponHistoryJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistory> {
        return couponHistoryJpaRepository.findByUserCouponIdOrderByRecordedAtDesc(userCouponId)
            .map { it.toDomain() }
    }
}
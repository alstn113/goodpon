package com.goodpon.infra.jpa.core

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.jpa.entity.CouponHistoryEntity
import com.goodpon.infra.jpa.repository.CouponHistoryJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class CouponHistoryCoreRepository(
    private val couponHistoryJpaRepository: CouponHistoryJpaRepository,
) {

    fun save(couponHistory: CouponHistory): CouponHistory {
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

    fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistory> {
        return couponHistoryJpaRepository.findByUserCouponIdOrderByRecordedAtDesc(userCouponId)
            .map { it.toDomain() }
    }
}
package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.infra.db.jpa.entity.CouponHistoryEntity
import com.goodpon.infra.db.jpa.repository.CouponHistoryJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponHistoryCoreRepository(
    private val couponHistoryJpaRepository: CouponHistoryJpaRepository,
) {

    @Transactional
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

    @Transactional(readOnly = true)
    fun findFirstByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): CouponHistory? {
        return couponHistoryJpaRepository.findFirstByUserCouponIdOrderByRecordedAtDesc(userCouponId)
            ?.toDomain()
    }
}
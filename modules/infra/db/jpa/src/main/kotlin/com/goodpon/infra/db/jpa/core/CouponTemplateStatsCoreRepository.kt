package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.infra.db.jpa.entity.CouponTemplateStatsEntity
import com.goodpon.infra.db.jpa.repository.CouponTemplateStatsJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponTemplateStatsCoreRepository(
    private val couponTemplateStatsJpaRepository: CouponTemplateStatsJpaRepository,
) {

    @Transactional
    fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats {
        val entity = couponTemplateStatsJpaRepository.findByIdOrNull(couponTemplateStats.couponTemplateId)
        if (entity == null) {
            val newEntity = CouponTemplateStatsEntity.fromDomain(couponTemplateStats)
            val savedEntity = couponTemplateStatsJpaRepository.save(newEntity)
            return savedEntity.toDomain()
        }

        entity.update(couponTemplateStats)
        val savedEntity = couponTemplateStatsJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    @Transactional(readOnly = true)
    fun findByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats? {
        return couponTemplateStatsJpaRepository.findByCouponTemplateIdForUpdate(couponTemplateId)
            ?.toDomain()
    }
}
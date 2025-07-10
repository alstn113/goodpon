package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.infra.db.jpa.entity.CouponTemplateStatsEntity
import com.goodpon.infra.db.jpa.repository.CouponTemplateStatsJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class CouponTemplateStatsCoreRepository(
    private val couponTemplateStatsJpaRepository: CouponTemplateStatsJpaRepository,
) {

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

    fun findByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats? {
        return couponTemplateStatsJpaRepository.findByCouponTemplateIdForUpdate(couponTemplateId)
            ?.toDomain()
    }
}
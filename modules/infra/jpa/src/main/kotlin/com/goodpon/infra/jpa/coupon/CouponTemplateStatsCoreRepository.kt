package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.CouponTemplateStats
import com.goodpon.core.domain.coupon.CouponTemplateStatsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponTemplateStatsCoreRepository(
    private val couponTemplateStatsJpaRepository: CouponTemplateStatsJpaRepository,
) : CouponTemplateStatsRepository {

    override fun save(couponTemplateStats: CouponTemplateStats): CouponTemplateStats {
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

    override fun findByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStats? {
        return couponTemplateStatsJpaRepository.findByCouponTemplateIdForUpdate(couponTemplateId)
            ?.toDomain()
    }
}
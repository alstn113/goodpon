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
            ?: throw IllegalArgumentException("CouponTemplateStats not found")
        entity.update(couponTemplateStats)
        val savedEntity = couponTemplateStatsJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByCouponTemplateIdWithLock(couponTemplateId: Long): CouponTemplateStats? {
        return couponTemplateStatsJpaRepository.findByCouponTemplateIdWithLock(couponTemplateId)
            ?.toDomain()
    }
}
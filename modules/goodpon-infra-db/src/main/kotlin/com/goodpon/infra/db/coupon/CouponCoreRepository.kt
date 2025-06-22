package com.goodpon.infra.db.coupon

import com.goodpon.core.domain.coupon.Coupon
import com.goodpon.core.domain.coupon.CouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponCoreRepository(
    private val couponJpaRepository: CouponJpaRepository,
) : CouponRepository {

    override fun save(coupon: Coupon): Coupon {
        val entity = couponJpaRepository.findByIdOrNull(coupon.id)
            ?: throw IllegalArgumentException("Coupon with id ${coupon.id} not found")
        entity.update(coupon)
        val savedEntity = couponJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByAccountIdAndCouponTemplateId(accountId: Long, couponTemplateId: Long): Coupon? {
        return couponJpaRepository.findFirstByAccountIdAndCouponTemplateId(accountId, couponTemplateId)
            ?.toDomain()
    }
}
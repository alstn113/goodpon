package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.IssuedCoupon
import com.goodpon.core.domain.coupon.IssuedCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class IssuedCouponCoreRepository(
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) : IssuedCouponRepository {

    override fun save(issuedCoupon: IssuedCoupon): IssuedCoupon {
        val entity = issuedCouponJpaRepository.findByIdOrNull(issuedCoupon.id)
            ?: throw IllegalArgumentException("Coupon with id ${issuedCoupon.id} not found")
        entity.update(issuedCoupon)
        val savedEntity = issuedCouponJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): IssuedCoupon? {
        return issuedCouponJpaRepository.findFirstByUserIdAndCouponTemplateId(userId, couponTemplateId)
            ?.toDomain()
    }

    override fun findByIdForUpdate(id: String): IssuedCoupon? {
        return issuedCouponJpaRepository.findByIdForUpdate(id)
            ?.toDomain()
    }
}
package com.goodpon.common.infra.persistence.coupon

import com.goodpon.common.domain.coupon.IssuedCoupon
import com.goodpon.common.domain.coupon.IssuedCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class IssuedCouponCoreRepository(
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) : IssuedCouponRepository {

    fun save(issuedCoupon: IssuedCoupon): IssuedCoupon {
        val entity = issuedCouponJpaRepository.findByIdOrNull(issuedCoupon.id)
            ?: throw IllegalArgumentException("IssuedCoupon with id ${issuedCoupon.id} not found")
        entity.update(issuedCoupon)
        val savedEntity = issuedCouponJpaRepository.save(entity)
        return savedEntity.toDomain()
    }
}
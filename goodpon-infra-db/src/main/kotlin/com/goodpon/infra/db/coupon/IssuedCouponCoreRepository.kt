package com.goodpon.infra.db.coupon

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
            ?: throw IllegalArgumentException("IssuedCoupon with id ${issuedCoupon.id} not found")
        entity.update(issuedCoupon)
        val savedEntity = issuedCouponJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByAccountIdAndCouponTemplateId(accountId: Long, couponTemplateId: Long): IssuedCoupon? {
        return issuedCouponJpaRepository.findFirstByAccountIdAndCouponTemplateId(accountId, couponTemplateId)
            ?.toDomain()
    }
}
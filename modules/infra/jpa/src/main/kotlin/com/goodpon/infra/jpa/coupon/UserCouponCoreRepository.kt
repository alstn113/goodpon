package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.UserCoupon
import com.goodpon.core.domain.coupon.UserCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserCouponCoreRepository(
    private val issuedCouponJpaRepository: UserCouponJpaRepository,
) : UserCouponRepository {

    override fun save(issuedCoupon: UserCoupon): UserCoupon {
        val entity = issuedCouponJpaRepository.findByIdOrNull(issuedCoupon.id)
            ?: throw IllegalArgumentException("Coupon with id ${issuedCoupon.id} not found")
        entity.update(issuedCoupon)
        val savedEntity = issuedCouponJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): UserCoupon? {
        return issuedCouponJpaRepository.findFirstByUserIdAndCouponTemplateId(userId, couponTemplateId)
            ?.toDomain()
    }

    override fun findByIdForUpdate(id: String): UserCoupon? {
        return issuedCouponJpaRepository.findByIdForUpdate(id)
            ?.toDomain()
    }
}
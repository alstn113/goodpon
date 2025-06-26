package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.UserCoupon
import com.goodpon.core.domain.coupon.UserCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserCouponCoreRepository(
    private val userCouponJpaRepository: UserCouponJpaRepository,
) : UserCouponRepository {

    override fun save(userCoupon: UserCoupon): UserCoupon {
        val entity = userCouponJpaRepository.findByIdOrNull(userCoupon.id)
        if (entity == null) {
            val newEntity = UserCouponEntity.fromDomain(userCoupon)
            val savedEntity = userCouponJpaRepository.save(newEntity)
            return savedEntity.toDomain()
        }

        entity.update(userCoupon)
        val savedEntity = userCouponJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findByIdForUpdate(id: String): UserCoupon? {
        return userCouponJpaRepository.findByIdForUpdate(id)
            ?.toDomain()
    }

    override fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponJpaRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }
}
package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.CouponStatus
import com.goodpon.core.domain.coupon.UserCoupon
import com.goodpon.core.domain.coupon.UserCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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

    override fun saveAll(userCoupons: List<UserCoupon>): List<UserCoupon> {
        val entities = userCoupons.map { UserCouponEntity.fromDomain(it) }
        val savedEntities = userCouponJpaRepository.saveAll(entities)
        return savedEntities.map { it.toDomain() }
    }

    override fun findByIdForUpdate(id: String): UserCoupon? {
        return userCouponJpaRepository.findByIdForUpdate(id)
            ?.toDomain()
    }

    override fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponJpaRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }

    override fun findByStatusAndExpiresAtLessThanEqual(
        status: CouponStatus,
        expiresAt: LocalDateTime,
    ): List<UserCoupon> {
        return userCouponJpaRepository.findByStatusAndExpiresAtLessThanEqual(status, expiresAt)
            .map { it.toDomain() }
    }
}
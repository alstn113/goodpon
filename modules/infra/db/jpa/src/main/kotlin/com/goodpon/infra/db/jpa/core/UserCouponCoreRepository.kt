package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.db.jpa.entity.UserCouponEntity
import com.goodpon.infra.db.jpa.repository.UserCouponJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserCouponCoreRepository(
    private val userCouponJpaRepository: UserCouponJpaRepository,
) {

    fun save(userCoupon: UserCoupon): UserCoupon {
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

    fun saveAll(userCoupons: List<UserCoupon>): List<UserCoupon> {
        val entities = userCoupons.map { UserCouponEntity.fromDomain(it) }
        val savedEntities = userCouponJpaRepository.saveAll(entities)
        return savedEntities.map { it.toDomain() }
    }

    fun findByIdForUpdate(id: String): UserCoupon? {
        return userCouponJpaRepository.findByIdForUpdate(id)
            ?.toDomain()
    }

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponJpaRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }

    fun findByStatusAndExpiresAtLessThanEqual(
        status: UserCouponStatus,
        expiresAt: LocalDateTime,
    ): List<UserCoupon> {
        return userCouponJpaRepository.findByStatusAndExpiresAtLessThanEqual(status, expiresAt)
            .map { it.toDomain() }
    }
}
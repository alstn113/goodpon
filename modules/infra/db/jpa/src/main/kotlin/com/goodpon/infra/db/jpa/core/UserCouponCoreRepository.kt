package com.goodpon.infra.db.jpa.core

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.db.jpa.entity.UserCouponEntity
import com.goodpon.infra.db.jpa.repository.UserCouponJpaRepository
import com.goodpon.infra.db.jpa.repository.dto.AvailableUserCouponViewDto
import com.goodpon.infra.db.jpa.repository.dto.UserCouponSummaryDto
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class UserCouponCoreRepository(
    private val userCouponJpaRepository: UserCouponJpaRepository,
    private val em: EntityManager
) {

    @Transactional
    fun save(userCoupon: UserCoupon): UserCoupon {
        val entity = userCouponJpaRepository.findByIdOrNull(userCoupon.id)
        if (entity == null) {
            val newEntity = UserCouponEntity.fromDomain(userCoupon)
            em.persist(newEntity)
            return newEntity.toDomain()
        }

        entity.update(userCoupon)
        return entity.toDomain()
    }

    @Transactional
    fun saveAll(userCoupons: List<UserCoupon>): List<UserCoupon> {
        return userCoupons.map { UserCouponEntity.fromDomain(it) }
            .map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    fun findByIdForUpdate(id: String): UserCoupon? {
        return userCouponJpaRepository.findByIdForUpdate(id)
            ?.toDomain()
    }

    @Transactional(readOnly = true)
    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponJpaRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }

    @Transactional(readOnly = true)
    fun findByStatusAndExpiresAtLessThanEqual(
        status: UserCouponStatus,
        expiresAt: LocalDateTime,
    ): List<UserCoupon> {
        return userCouponJpaRepository.findByStatusAndExpiresAtLessThanEqual(status, expiresAt)
            .map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    fun findUserCouponSummaries(userId: String, merchantId: Long): List<UserCouponSummaryDto> {
        return userCouponJpaRepository.findUserCouponSummaries(userId = userId, merchantId = merchantId)
    }

    @Transactional(readOnly = true)
    fun findAvailableUserCouponsForOrderAmount(
        userId: String,
        merchantId: Long,
        orderAmount: Int,
    ): List<AvailableUserCouponViewDto> {
        return userCouponJpaRepository.findAvailableUserCouponsForOrderAmount(
            userId = userId,
            merchantId = merchantId,
            orderAmount = orderAmount,
        )
    }
}
package com.goodpon.infra.jpa.coupon

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import com.goodpon.dashboard.application.coupon.port.out.UserCouponRepository as Dashboard_UserCouponRepository
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository as Partner_UserCouponRepository

@Repository
class UserCouponCoreRepository(
    private val userCouponJpaRepository: UserCouponJpaRepository,
) : Partner_UserCouponRepository, Dashboard_UserCouponRepository {

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
        status: UserCouponStatus,
        expiresAt: LocalDateTime,
    ): List<UserCoupon> {
        return userCouponJpaRepository.findByStatusAndExpiresAtLessThanEqual(status, expiresAt)
            .map { it.toDomain() }
    }
}
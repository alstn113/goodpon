package com.goodpon.infra.jpa.core

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.jpa.entity.UserCouponEntity
import com.goodpon.infra.jpa.repository.UserCouponJpaRepository
import com.goodpon.infra.jpa.repository.dto.AvailableUserCouponViewDto
import com.goodpon.infra.jpa.repository.dto.UserCouponSummaryDto
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime

@Repository
class UserCouponCoreRepository(
    private val userCouponJpaRepository: UserCouponJpaRepository,
    private val em: EntityManager,
    private val jdbcTemplate: JdbcTemplate,
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
    fun saveAll(userCoupons: List<UserCoupon>) {
        val entities = userCoupons.map { UserCouponEntity.fromDomain(it) }
        val now = Timestamp.valueOf(LocalDateTime.now())

        val sql = """
        INSERT INTO user_coupons (
            id, 
            user_id, 
            coupon_template_id, 
            status, 
            issued_at, 
            expires_at, 
            redeemed_at, 
            created_at, 
            updated_at
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.trimIndent()

        jdbcTemplate.batchUpdate(
            sql,
            entities,
            entities.size
        ) { ps, entity ->
            ps.setString(1, entity.id)
            ps.setString(2, entity.userId)
            ps.setLong(3, entity.couponTemplateId)
            ps.setString(4, entity.status.name)
            ps.setTimestamp(5, Timestamp.valueOf(entity.issuedAt))
            ps.setObject(6, entity.expiresAt?.let { Timestamp.valueOf(it) }, java.sql.Types.TIMESTAMP) // nullable
            ps.setObject(7, entity.redeemedAt?.let { Timestamp.valueOf(it) }, java.sql.Types.TIMESTAMP) // nullable
            ps.setTimestamp(8, now)
            ps.setTimestamp(9, now)
        }
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

    @Transactional(readOnly = true)
    fun findAllByUserIdInAndCouponTemplateIdIn(userIds: List<String>, couponTemplateIds: List<Long>): List<UserCoupon> {
        return userCouponJpaRepository.findAllByUserIdInAndCouponTemplateIdIn(userIds, couponTemplateIds)
            .map { it.toDomain() }
    }
}
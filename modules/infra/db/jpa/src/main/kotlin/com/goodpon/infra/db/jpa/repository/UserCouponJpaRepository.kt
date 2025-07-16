package com.goodpon.infra.db.jpa.repository

import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.db.jpa.entity.UserCouponEntity
import com.goodpon.infra.db.jpa.repository.dto.UserCouponViewDto
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface UserCouponJpaRepository : JpaRepository<UserCouponEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ic FROM UserCouponEntity ic WHERE ic.id = :id")
    fun findByIdForUpdate(id: String): UserCouponEntity?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findByStatusAndExpiresAtLessThanEqual(
        status: UserCouponStatus,
        expiresAt: LocalDateTime,
    ): List<UserCouponEntity>

    @Query(
        """
        SELECT new com.goodpon.infra.db.jpa.repository.dto.UserCouponViewDto(
            userCoupon.id,
            userCoupon.couponTemplateId,
            couponTemplate.name,
            couponTemplate.description,
            couponTemplate.discountType,
            couponTemplate.discountValue,
            couponTemplate.maxDiscountAmount,
            couponTemplate.minOrderAmount,
            userCoupon.issuedAt,
            userCoupon.expiresAt,
            couponTemplate.limitType,
            couponTemplate.maxIssueCount,
            couponTemplate.maxRedeemCount,
            (couponTemplate.maxRedeemCount IS NULL OR (couponTemplateStats.redeemCount < couponTemplate.maxRedeemCount))
        )
        FROM UserCouponEntity userCoupon
        JOIN CouponTemplateEntity couponTemplate
            ON userCoupon.couponTemplateId = couponTemplate.id
        LEFT JOIN CouponTemplateStatsEntity couponTemplateStats
            ON couponTemplate.id = couponTemplateStats.couponTemplateId
        WHERE userCoupon.userId = :userId 
            AND userCoupon.status = :userCouponStatus
            AND couponTemplate.merchantId = :merchantId
        """
    )
    fun findIssuedUserCouponsByUserId(
        userId: String,
        merchantId: Long,
        userCouponStatus: UserCouponStatus = UserCouponStatus.ISSUED,
    ): List<UserCouponViewDto>
}

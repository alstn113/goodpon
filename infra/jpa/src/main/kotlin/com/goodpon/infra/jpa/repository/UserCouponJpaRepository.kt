package com.goodpon.infra.jpa.repository

import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.jpa.entity.UserCouponEntity
import com.goodpon.infra.jpa.repository.dto.AvailableUserCouponViewDto
import com.goodpon.infra.jpa.repository.dto.UserCouponSummaryDto
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface UserCouponJpaRepository : JpaRepository<UserCouponEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT userCoupon FROM UserCouponEntity userCoupon WHERE userCoupon.id = :id")
    fun findByIdForUpdate(id: String): UserCouponEntity?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findByStatusAndExpiresAtLessThanEqual(
        status: UserCouponStatus,
        expiresAt: LocalDateTime,
    ): List<UserCouponEntity>

    @Query(
        """
        SELECT new com.goodpon.infra.jpa.repository.dto.UserCouponSummaryDto(
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
            couponTemplate.maxRedeemCount
        )
        FROM UserCouponEntity userCoupon
            JOIN CouponTemplateEntity couponTemplate
                ON userCoupon.couponTemplateId = couponTemplate.id
        WHERE userCoupon.userId = :userId 
            AND userCoupon.status = :userCouponStatus
            AND couponTemplate.merchantId = :merchantId
        ORDER BY 
            userCoupon.expiresAt ASC,
            userCoupon.issuedAt DESC
        """
    )
    fun findUserCouponSummaries(
        userId: String,
        merchantId: Long,
        userCouponStatus: UserCouponStatus = UserCouponStatus.ISSUED,
    ): List<UserCouponSummaryDto>

    @Query(
        """
        SELECT new com.goodpon.infra.jpa.repository.dto.AvailableUserCouponViewDto(
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
            CASE 
                WHEN couponTemplate.minOrderAmount IS NULL 
                    OR couponTemplate.minOrderAmount <= :orderAmount 
                THEN true ELSE false 
            END
        )
        FROM UserCouponEntity userCoupon
            JOIN CouponTemplateEntity couponTemplate
                ON userCoupon.couponTemplateId = couponTemplate.id
        WHERE userCoupon.userId = :userId 
            AND userCoupon.status = :userCouponStatus
            AND couponTemplate.merchantId = :merchantId
        ORDER BY 
            CASE 
                WHEN couponTemplate.minOrderAmount IS NULL 
                    OR couponTemplate.minOrderAmount <= :orderAmount 
                THEN 1 ELSE 0 
            END DESC,
            userCoupon.expiresAt ASC,
            userCoupon.issuedAt DESC
        """
    )
    fun findAvailableUserCouponsForOrderAmount(
        userId: String,
        merchantId: Long,
        orderAmount: Int,
        userCouponStatus: UserCouponStatus = UserCouponStatus.ISSUED,
    ): List<AvailableUserCouponViewDto>
}
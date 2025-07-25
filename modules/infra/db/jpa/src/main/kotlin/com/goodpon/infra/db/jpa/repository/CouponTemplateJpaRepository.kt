package com.goodpon.infra.db.jpa.repository

import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.db.jpa.entity.CouponTemplateEntity
import com.goodpon.infra.db.jpa.repository.dto.CouponTemplateDetailDto
import com.goodpon.infra.db.jpa.repository.dto.CouponTemplateSummaryDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface CouponTemplateJpaRepository : JpaRepository<CouponTemplateEntity, Long> {

    fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplateEntity>

    @Query(
        """
        SELECT new com.goodpon.infra.db.jpa.repository.dto.CouponTemplateSummaryDto(
            couponTemplate.id,
            couponTemplate.name,
            couponTemplate.description,
            couponTemplate.status,
            stats.issueCount,
            stats.redeemCount,
            couponTemplate.createdAt
        )
        FROM CouponTemplateEntity couponTemplate
        LEFT JOIN CouponTemplateStatsEntity stats
            ON stats.couponTemplateId = couponTemplate.id
        WHERE couponTemplate.merchantId = :merchantId
        ORDER BY couponTemplate.createdAt DESC
        """
    )
    fun findCouponTemplateSummaries(merchantId: Long): List<CouponTemplateSummaryDto>

    @Query(
        """
        SELECT new com.goodpon.infra.db.jpa.repository.dto.CouponTemplateDetailDto(
            couponTemplate.id,
            couponTemplate.merchantId,
            couponTemplate.name,
            couponTemplate.description,
            couponTemplate.minOrderAmount,
            couponTemplate.discountType,
            couponTemplate.discountValue,
            couponTemplate.maxDiscountAmount,
            couponTemplate.status,
            couponTemplate.issueStartAt,
            couponTemplate.issueEndAt,
            couponTemplate.validityDays,
            couponTemplate.absoluteExpiresAt,
            couponTemplate.limitType,
            couponTemplate.maxIssueCount,
            couponTemplate.maxRedeemCount,
            stats.issueCount,
            stats.redeemCount,
            couponTemplate.createdAt
        )
        FROM CouponTemplateEntity couponTemplate
        LEFT JOIN CouponTemplateStatsEntity stats
            ON stats.couponTemplateId = couponTemplate.id
        WHERE couponTemplate.id = :couponTemplateId
        """
    )
    fun findByIdWithStats(couponTemplateId: Long): CouponTemplateDetailDto?

    @Query(
        """
        SELECT new com.goodpon.infra.db.jpa.repository.dto.CouponTemplateDetailDto(
            couponTemplate.id,
            couponTemplate.merchantId,
            couponTemplate.name,
            couponTemplate.description,
            couponTemplate.minOrderAmount,
            couponTemplate.discountType,
            couponTemplate.discountValue,
            couponTemplate.maxDiscountAmount,
            couponTemplate.status,
            couponTemplate.issueStartAt,
            couponTemplate.issueEndAt,
            couponTemplate.validityDays,
            couponTemplate.absoluteExpiresAt,
            couponTemplate.limitType,
            couponTemplate.maxIssueCount,
            couponTemplate.maxRedeemCount,
            stats.issueCount,
            stats.redeemCount,
            couponTemplate.createdAt
        ) 
        FROM CouponTemplateEntity couponTemplate
        LEFT JOIN CouponTemplateStatsEntity stats
            ON stats.couponTemplateId = couponTemplate.id
        WHERE couponTemplate.id = :couponTemplateId 
            AND couponTemplate.merchantId = :merchantId
            AND couponTemplate.status = :couponStatus
        ORDER BY couponTemplate.createdAt DESC
        """
    )
    fun findByIdAndMerchantIdWithStats(
        couponTemplateId: Long,
        merchantId: Long,
        couponStatus: CouponTemplateStatus = CouponTemplateStatus.ISSUABLE,
    ): CouponTemplateDetailDto?
}

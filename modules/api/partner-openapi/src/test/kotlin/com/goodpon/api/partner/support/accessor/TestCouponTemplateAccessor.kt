package com.goodpon.api.partner.support.accessor

import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.db.jpa.entity.CouponTemplateEntity
import com.goodpon.infra.db.jpa.entity.CouponTemplateStatsEntity
import jakarta.persistence.EntityManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class TestCouponTemplateAccessor(
    private val entityManager: EntityManager,
    private val redisTemplate: RedisTemplate<String, String>,
) {

    @Transactional
    fun createCouponTemplate(
        merchantId: Long,
        name: String = "테스트 쿠폰 템플릿",
        minOrderAmount: Int? = 15000,
        discountType: CouponDiscountType = CouponDiscountType.FIXED_AMOUNT,
        discountValue: Int = 2000,
        maxDiscountAmount: Int? = null,
        issueStartAt: LocalDateTime = LocalDateTime.now(),
        issueEndAt: LocalDateTime? = LocalDateTime.now().plusDays(10),
        validityDays: Int? = null,
        absoluteExpiresAt: LocalDateTime? = LocalDateTime.now().plusDays(20),
        limitType: CouponLimitPolicyType = CouponLimitPolicyType.ISSUE_COUNT,
        maxIssueCount: Long? = 10,
        maxRedeemCount: Long? = null,
        status: CouponTemplateStatus = CouponTemplateStatus.ISSUABLE,
    ): Long {
        val couponTemplate = CouponTemplateEntity(
            merchantId = merchantId,
            name = name,
            description = "테스트 쿠폰 템플릿 설명",
            minOrderAmount = minOrderAmount,
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount,
            issueStartAt = issueStartAt,
            issueEndAt = issueEndAt,
            validityDays = validityDays,
            absoluteExpiresAt = absoluteExpiresAt,
            limitType = limitType,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = maxRedeemCount,
            status = status,
        )
        entityManager.persist(couponTemplate)

        val couponTemplateStats = CouponTemplateStatsEntity(
            couponTemplateId = couponTemplate.id,
            issueCount = 0L,
            redeemCount = 0L,
        )
        entityManager.persist(couponTemplateStats)

        redisTemplate.opsForHash<String, String>().putAll(
            "coupon-template-stats:${couponTemplate.id}",
            mapOf(
                "issueCount" to "0",
                "redeemCount" to "0",
            )
        )

        return couponTemplate.id
    }

    @Transactional(readOnly = true)
    fun countUserCoupons(couponTemplateId: Long): Long {
        return entityManager.createQuery(
            "SELECT COUNT(uc) FROM UserCouponEntity uc WHERE uc.couponTemplateId = :couponTemplateId",
            Long::class.java
        ).setParameter("couponTemplateId", couponTemplateId).singleResult ?: 0L
    }
}
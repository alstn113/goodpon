package com.goodpon.core.domain.coupon

import java.time.LocalDate
import java.time.LocalDateTime

data class CouponTemplate private constructor(
    val id: Long,
    val merchantId: Long,
    val name: String,
    val description: String,
    val usageCondition: CouponUsageCondition,
    val discountPolicy: DiscountPolicy,
    val couponPeriod: CouponPeriod,
    val usageLimitPolicy: UsageLimitPolicy,
    val status: CouponTemplateStatus,
    val isIssuable: Boolean,
    val isUsable: Boolean,
) {

    fun calculateFinalUseEndAt(now: LocalDate): LocalDateTime? {
        return couponPeriod.calculateFinalUseEndAt(now)
    }

    fun checkIssuePossible(issueCount: Long, now: LocalDateTime = LocalDateTime.now()): Result<Unit> {
        if (!couponPeriod.isIssuable(now)) {
            return Result.failure(IllegalStateException("쿠폰을 발급할 수 있는 기간이 아닙니다."))
        }
        if (status.isNotIssuable() || !isIssuable) {
            return Result.failure(IllegalStateException("쿠폰을 발급할 수 있는 상태가 아닙니다."))
        }
        if (!usageLimitPolicy.isIssuable(issueCount)) {
            return Result.failure(IllegalStateException("쿠폰 발급 한도를 초과했습니다."))
        }
        return Result.success(Unit)
    }

    fun checkUsePossible(useCount: Long): Result<Unit> {
        if (status.isNotUsable() || !isUsable) {
            return Result.failure(IllegalStateException("쿠폰을 사용할 수 있는 상태가 아닙니다."))
        }
        if (!usageLimitPolicy.isUsable(useCount)) {
            return Result.failure(IllegalStateException("쿠폰 사용 한도를 초과했습니다."))
        }
        return Result.success(Unit)
    }

    companion object {

        fun create(
            merchantId: Long,
            name: String,
            description: String,
            minimumOrderAmount: Long? = null,
            discountType: DiscountType,
            discountValue: Int,
            issueStartAt: LocalDateTime,
            issueEndAt: LocalDateTime?,
            validityDays: Int?,
            useEndAt: LocalDateTime?,
            limitType: CouponTemplateLimitType,
            issueLimit: Long? = null,
            useLimit: Long? = null,
        ): CouponTemplate {
            return CouponTemplate(
                id = 0,
                merchantId = merchantId,
                name = name,
                description = description,
                usageCondition = CouponUsageCondition(minimumOrderAmount),
                discountPolicy = DiscountPolicy(discountType, discountValue),
                couponPeriod = CouponPeriod.create(
                    issueStartDate = issueStartAt.toLocalDate(),
                    issueEndDate = issueEndAt?.toLocalDate(),
                    validityDays = validityDays,
                    useEndDate = useEndAt?.toLocalDate()
                ),
                usageLimitPolicy = UsageLimitPolicy(
                    limitType = limitType,
                    issueLimit = issueLimit,
                    useLimit = useLimit
                ),
                status = CouponTemplateStatus.DRAFT,
                isIssuable = true,
                isUsable = true
            )
        }

        fun reconstitute(
            id: Long,
            merchantId: Long,
            name: String,
            description: String,
            minimumOrderAmount: Long? = null,
            discountType: DiscountType,
            discountValue: Int,
            issueStartAt: LocalDateTime,
            issueEndAt: LocalDateTime?,
            validityDays: Int?,
            useEndAt: LocalDateTime?,
            limitType: CouponTemplateLimitType,
            issueLimit: Long? = null,
            useLimit: Long? = null,
            status: CouponTemplateStatus,
            isIssuable: Boolean,
            isUsable: Boolean,
        ): CouponTemplate {
            return CouponTemplate(
                id = id,
                merchantId = merchantId,
                name = name,
                description = description,
                usageCondition = CouponUsageCondition(minimumOrderAmount),
                discountPolicy = DiscountPolicy(discountType, discountValue),
                couponPeriod = CouponPeriod.create(
                    issueStartDate = issueStartAt.toLocalDate(),
                    issueEndDate = issueEndAt?.toLocalDate(),
                    validityDays = validityDays,
                    useEndDate = useEndAt?.toLocalDate()
                ),
                usageLimitPolicy = UsageLimitPolicy(
                    limitType = limitType,
                    issueLimit = issueLimit,
                    useLimit = useLimit
                ),
                status = status,
                isIssuable = isIssuable,
                isUsable = isUsable
            )
        }
    }
}

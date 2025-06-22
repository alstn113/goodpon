package com.goodpon.core.domain.coupon

import com.goodpon.core.domain.coupon.vo.*
import java.time.LocalDate
import java.time.LocalDateTime

data class CouponTemplate(
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
}

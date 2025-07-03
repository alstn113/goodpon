package com.goodpon.core.domain.coupon.template

import com.goodpon.core.domain.coupon.template.vo.*
import java.time.LocalDate
import java.time.LocalDateTime

data class CouponTemplate(
    val id: Long,
    val merchantId: Long,
    val name: String,
    val description: String,
    val redemptionCondition: CouponRedemptionCondition,
    val discountPolicy: CouponDiscountPolicy,
    val period: CouponPeriod,
    val limitPolicy: CouponLimitPolicy,
    val status: CouponTemplateStatus,
) {
    fun validateIssue(currentIssuedCount: Long, now: LocalDateTime = LocalDateTime.now()): Result<Unit> {
        if (!period.isIssuable(now)) {
            return Result.failure(IllegalStateException("쿠폰을 발급할 수 있는 기간이 아닙니다."))
        }
        if (status.isNotIssuable()) {
            return Result.failure(IllegalStateException("쿠폰을 발급할 수 있는 상태가 아닙니다."))
        }
        if (!limitPolicy.canIssue(currentIssuedCount)) {
            return Result.failure(IllegalStateException("쿠폰 발급 한도를 초과했습니다."))
        }
        return Result.success(Unit)
    }

    fun validateRedeem(currentRedeemedCount: Long): Result<Unit> {
        if (status.isNotRedeemable()) {
            return Result.failure(IllegalStateException("쿠폰을 사용할 수 있는 상태가 아닙니다."))
        }
        if (!limitPolicy.canRedeem(currentRedeemedCount)) {
            return Result.failure(IllegalStateException("쿠폰 사용 한도를 초과했습니다."))
        }
        return Result.success(Unit)
    }

    fun validateOwnership(merchantId: Long) {
        if (this.merchantId != merchantId) {
            throw IllegalArgumentException("쿠폰 템플릿을 발급할 권한이 없습니다.")
        }
    }

    fun calculateExpiresAt(now: LocalDate): LocalDateTime? {
        return period.calculateExpiresAt(now)
    }

    fun calculateDiscountAmount(orderAmount: Int): Int {
        return discountPolicy.calculateDiscountAmount(orderAmount)
    }

    fun calculateFinalPrice(orderAmount: Int): Int {
        return orderAmount - calculateDiscountAmount(orderAmount)
    }

    fun expire(): CouponTemplate {
        return this.copy(status = CouponTemplateStatus.EXPIRED)
    }
}

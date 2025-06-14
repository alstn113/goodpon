package com.goodpon.common.domain.coupon

import java.time.LocalDateTime

data class CouponTemplate(
    val id: Long,
    val merchantId: Long,
    val name: String,
    val description: String,
    val usageCondition: CouponUsageCondition,
    val discountPolicy: DiscountPolicy,
    val couponPeriod: CouponPeriod,
    val usageLimit: UsageLimit,
    val status: CouponTemplateStatus, // 쿠폰의 주요한 생명 주기
    val isIssuable: Boolean, // 보조 제어 플래그
    val isUsable: Boolean, // 보조 제어 플래스
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    fun issue(now: LocalDateTime = LocalDateTime.now()): CouponTemplate {
        return validateIsIssuable(now)
            .fold(
                onSuccess = {
                    copy(
                        usageLimit = usageLimit.recordIssuance(),
                    )
                },
                onFailure = { throw it }
            )
    }

    fun use(): CouponTemplate {
        return validateIsUsable()
            .fold(
                onSuccess = {
                    copy(
                        usageLimit = usageLimit.recordUsage(),
                    )
                },
                onFailure = { throw it }
            )
    }

    fun isIssuable(now: LocalDateTime): Boolean {
        return validateIsIssuable(now).isSuccess
    }

    fun isUsable(): Boolean {
        return validateIsUsable().isSuccess
    }

    private fun validateIsIssuable(now: LocalDateTime): Result<Unit> {
        if (!couponPeriod.isIssuable(now)) {
            return Result.failure(IllegalStateException("쿠폰을 발급할 수 있는 기간이 아닙니다."))
        }
        if (status.isNotIssuable() || !isIssuable) {
            return Result.failure(IllegalStateException("쿠폰을 발급할 수 있는 상태가 아닙니다."))
        }
        if (!usageLimit.isIssuable()) {
            return Result.failure(IllegalStateException("쿠폰 발급 한도를 초과했습니다."))
        }
        return Result.success(Unit)
    }

    private fun validateIsUsable(): Result<Unit> {
        if (status.isNotUsable() || !isUsable) {
            return Result.failure(IllegalStateException("쿠폰을 사용할 수 있는 상태가 아닙니다."))
        }
        if (!usageLimit.isUsable()) {
            return Result.failure(IllegalStateException("쿠폰 사용 한도를 초과했습니다."))
        }
        return Result.success(Unit)
    }
}

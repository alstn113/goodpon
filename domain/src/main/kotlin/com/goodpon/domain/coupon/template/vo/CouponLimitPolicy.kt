package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.creation.*

data class CouponLimitPolicy(
    val limitType: CouponLimitPolicyType,
    val maxIssueCount: Long? = null,
    val maxRedeemCount: Long? = null,
) {

    fun canIssue(currentIssuedCount: Long): Boolean {
        return when (limitType) {
            CouponLimitPolicyType.ISSUE_COUNT -> currentIssuedCount < maxIssueCount!!
            else -> true
        }
    }

    fun canRedeem(currentRedeemedCount: Long): Boolean {
        return when (limitType) {
            CouponLimitPolicyType.REDEEM_COUNT -> currentRedeemedCount < maxRedeemCount!!
            else -> true
        }
    }

    fun validate(): Result<Unit> {
        return when (limitType) {
            CouponLimitPolicyType.ISSUE_COUNT -> validateIssueCountLimit(maxIssueCount)
            CouponLimitPolicyType.REDEEM_COUNT -> validateRedeemCountLimit(maxRedeemCount)
            CouponLimitPolicyType.NONE -> validateNoneLimit()
        }
    }

    private fun validateIssueCountLimit(maxIssueCount: Long?): Result<Unit> {
        if (maxIssueCount == null || maxIssueCount <= 0) {
            return Result.failure(CouponLimitPolicyInvalidIssueValueException(maxIssueCount = maxIssueCount))
        }
        if (maxRedeemCount != null) {
            return Result.failure(CouponLimitPolicyIssueRedeemConflictException(maxRedeemCount = maxRedeemCount))
        }
        return Result.success(Unit)
    }

    private fun validateRedeemCountLimit(maxRedeemCount: Long?): Result<Unit> {
        if (maxRedeemCount == null || maxRedeemCount <= 0) {
            return Result.failure(CouponLimitPolicyInvalidRedeemValueException(maxRedeemCount = maxRedeemCount))
        }
        if (maxIssueCount != null) {
            return Result.failure(CouponLimitPolicyRedeemIssueConflictException(maxIssueCount = maxIssueCount))
        }
        return Result.success(Unit)

    }

    private fun validateNoneLimit(): Result<Unit> {
        if (maxIssueCount != null || maxRedeemCount != null) {
            return Result.failure(CouponLimitPolicyNoneConflictException())
        }
        return Result.success(Unit)
    }
}

package com.goodpon.core.domain.coupon.template.vo

import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType

data class CouponLimitPolicy(
    val limitType: CouponLimitPolicyType,
    val maxIssueCount: Long? = null,
    val maxRedeemCount: Long? = null,
) {
    init {
        when (limitType) {
            CouponLimitPolicyType.ISSUE_COUNT -> validateIssueCountLimit(maxIssueCount)
            CouponLimitPolicyType.REDEEM_COUNT -> validateRedeemCountLimit(maxRedeemCount)
            CouponLimitPolicyType.NONE -> validateNoneLimit()
        }
    }

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

    private fun validateIssueCountLimit(maxIssueCount: Long?) {
        if (maxIssueCount == null || maxIssueCount <= 0) {
            throw CoreException(ErrorType.INVALID_COUPON_LIMIT_POLICY_ISSUE_VALUE)
        }
        if (maxRedeemCount != null) {
            throw CoreException(ErrorType.INVALID_COUPON_LIMIT_POLICY_ISSUE_CONFLICT)
        }
    }

    private fun validateRedeemCountLimit(maxRedeemCount: Long?) {
        if (maxRedeemCount == null || maxRedeemCount <= 0) {
            throw CoreException(ErrorType.INVALID_COUPON_LIMIT_POLICY_REDEEM_VALUE)
        }
        if (maxIssueCount != null) {
            throw CoreException(ErrorType.INVALID_COUPON_LIMIT_POLICY_REDEEM_CONFLICT)
        }
    }

    private fun validateNoneLimit() {
        if (maxIssueCount != null || maxRedeemCount != null) {
            throw CoreException(ErrorType.INVALID_COUPON_LIMIT_POLICY_NONE_CONFLICT)
        }
    }
}

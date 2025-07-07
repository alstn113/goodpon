package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.coupon.template.exception.*

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
            throw CouponLimitPolicyInvalidIssueValueException()
        }
        if (maxRedeemCount != null) {
            throw CouponLimitPolicyIssueRedeemConflictException()
        }
    }

    private fun validateRedeemCountLimit(maxRedeemCount: Long?) {
        if (maxRedeemCount == null || maxRedeemCount <= 0) {
            throw CouponLimitPolicyInvalidRedeemValueException()
        }
        if (maxIssueCount != null) {
            throw CouponLimitPolicyRedeemIssueConflictException()
        }
    }

    private fun validateNoneLimit() {
        if (maxIssueCount != null || maxRedeemCount != null) {
            throw CouponLimitPolicyNoneConflictException()
        }
    }
}

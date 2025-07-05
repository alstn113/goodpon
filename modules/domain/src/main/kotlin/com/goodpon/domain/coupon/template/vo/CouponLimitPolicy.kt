package com.goodpon.domain.coupon.template.vo

import com.goodpon.domain.domain.coupon.template.exception.*

data class CouponLimitPolicy(
    val limitType: com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType,
    val maxIssueCount: Long? = null,
    val maxRedeemCount: Long? = null,
) {

    init {
        when (limitType) {
            com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType.ISSUE_COUNT -> validateIssueCountLimit(maxIssueCount)
            com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType.REDEEM_COUNT -> validateRedeemCountLimit(maxRedeemCount)
            com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType.NONE -> validateNoneLimit()
        }
    }

    fun canIssue(currentIssuedCount: Long): Boolean {
        return when (limitType) {
            com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType.ISSUE_COUNT -> currentIssuedCount < maxIssueCount!!
            else -> true
        }
    }

    fun canRedeem(currentRedeemedCount: Long): Boolean {
        return when (limitType) {
            com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType.REDEEM_COUNT -> currentRedeemedCount < maxRedeemCount!!
            else -> true
        }
    }

    private fun validateIssueCountLimit(maxIssueCount: Long?) {
        if (maxIssueCount == null || maxIssueCount <= 0) {
            throw CouponLimitPolicyInvalidIssueValueException()
        }
        if (maxRedeemCount != null) {
            throw com.goodpon.domain.coupon.template.exception.CouponLimitPolicyIssueRedeemConflictException()
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

package com.goodpon.domain.coupon.template

import com.goodpon.domain.coupon.template.exception.*
import com.goodpon.domain.coupon.template.vo.*
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

    fun validateIssue(currentIssuedCount: Long, issueAt: LocalDateTime) {
        if (!period.isIssuable(issueAt)) {
            throw CouponTemplateIssuancePeriodException()
        }
        if (status.isNotIssuable()) {
            throw CouponTemplateStatusNotIssuableException()
        }
        if (!limitPolicy.canIssue(currentIssuedCount)) {
            throw CouponTemplateIssuanceLimitExceededException()
        }
    }

    fun validateRedeem(currentRedeemedCount: Long, orderAmount: Int) {
        if (status.isNotRedeemable()) {
            throw CouponTemplateStatusNotRedeemableException()
        }
        if (!limitPolicy.canRedeem(currentRedeemedCount)) {
            throw CouponTemplateRedemptionLimitExceededException()
        }
        if (!redemptionCondition.isSatisfiedBy(orderAmount)) {
            throw CouponTemplateRedemptionConditionNotSatisfiedException()
        }
    }

    fun isOwnedBy(merchantId: Long): Boolean {
        return this.merchantId == merchantId
    }

    fun calculateExpiresAt(issueDate: LocalDate): LocalDateTime? {
        return period.calculateExpiresAt(issueDate)
    }

    fun calculateDiscountAmount(orderAmount: Int): Int {
        return discountPolicy.calculateDiscountAmount(orderAmount)
    }

    fun publish(): CouponTemplate {
        if (status.isNotDraft()) {
            throw CouponTemplateInvalidStatusToPublishException()
        }
        return this.copy(status = CouponTemplateStatus.ISSUABLE)
    }

    fun expire(): CouponTemplate {
        if (status.isNotIssuable()) {
            throw CouponTemplateExpirationNotAllowedException()
        }
        return this.copy(status = CouponTemplateStatus.EXPIRED)
    }
}

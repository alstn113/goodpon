package com.goodpon.domain.coupon.template

import com.goodpon.domain.coupon.template.exception.creation.*
import com.goodpon.domain.coupon.template.vo.*
import java.time.LocalDate
import java.time.LocalDateTime

object CouponTemplateFactory {

    fun create(
        merchantId: Long,
        name: String,
        description: String,
        minOrderAmount: Int?,
        discountType: CouponDiscountType,
        discountValue: Int,
        maxDiscountAmount: Int?,
        issueStartDate: LocalDate,
        issueEndDate: LocalDate?,
        validityDays: Int?,
        absoluteExpiryDate: LocalDate?,
        limitType: CouponLimitPolicyType,
        maxIssueCount: Long?,
        maxRedeemCount: Long?,
        status: CouponTemplateStatus = CouponTemplateStatus.DRAFT,
    ): CouponTemplate {
        val redemptionCondition = CouponRedemptionCondition(minOrderAmount = minOrderAmount)
        val discountPolicy = CouponDiscountPolicy(
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount
        )
        val periodResult = CouponPeriodFactory.create(
            issueStartDate = issueStartDate,
            issueEndDate = issueEndDate,
            validityDays = validityDays,
            absoluteExpiryDate = absoluteExpiryDate
        )
        val limitPolicy = CouponLimitPolicy(
            limitType = limitType,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = maxRedeemCount
        )

        val errors = mutableListOf<CouponTemplateValidationError>()
        addCouponRedemptionConditionErrors(errors, redemptionCondition)
        addCouponDiscountPolicyErrors(errors, discountPolicy)
        addCouponPeriodErrors(errors, periodResult)
        addCouponLimitPolicyErrors(errors, limitPolicy)

        if (errors.isNotEmpty()) {
            throw CouponTemplateValidationException(errors)
        }

        return CouponTemplate(
            id = 0,
            merchantId = merchantId,
            name = name,
            description = description,
            redemptionCondition = redemptionCondition,
            discountPolicy = discountPolicy,
            period = periodResult.getOrThrow(),
            limitPolicy = limitPolicy,
            status = status,
        )
    }

    private fun addCouponRedemptionConditionErrors(
        errors: MutableList<CouponTemplateValidationError>,
        redemptionCondition: CouponRedemptionCondition,
    ) {
        redemptionCondition.validate().onFailure { exception ->
            when (exception) {
                is CouponRedemptionConditionInvalidMinOrderAmountException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "minOrderAmount",
                        rejectedValue = exception.minOrderAmount,
                        message = "쿠폰 사용 조건의 최소 주문 금액은 0보다 커야 합니다."
                    )
                    errors.add(errorDetail)
                }
            }
        }
    }

    private fun addCouponDiscountPolicyErrors(
        errors: MutableList<CouponTemplateValidationError>,
        discountPolicy: CouponDiscountPolicy,
    ) {
        discountPolicy.validate().onFailure { exception ->
            when (exception) {
                is CouponDiscountPolicyInvalidFixedValueException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "discountValue",
                        rejectedValue = exception.discountValue,
                        message = "고정 할인 금액은 0보다 커야 합니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponDiscountPolicyInvalidFixedMaxException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "maxDiscountAmount",
                        rejectedValue = exception.maxDiscountAmount,
                        message = "고정 금액 할인은 최대 할인 금액을 설정할 수 없습니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponDiscountPolicyInvalidPercentValueException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "discountValue",
                        rejectedValue = exception.discountValue,
                        message = "백분율 할인 값은 1~100 사이여야 합니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponDiscountPolicyInvalidPercentMaxException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "maxDiscountAmount",
                        rejectedValue = exception.maxDiscountAmount,
                        message = "백분율 할인은 최대 할인 금액을 0보다 크게 설정해야 합니다."
                    )
                    errors.add(errorDetail)
                }
            }
        }
    }

    private fun addCouponPeriodErrors(
        errors: MutableList<CouponTemplateValidationError>,
        periodResult: Result<CouponPeriod>,
    ) {
        periodResult.onFailure { exception ->
            when (exception) {
                is CouponPeriodInvalidIssueEndBeforeStartException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "issueEndDate",
                        rejectedValue = exception.issueEndDate,
                        message = "쿠폰 발행 종료일은 발행 시작일보다 이전일 수 없습니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponPeriodInvalidExpireBeforeStartException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "absoluteExpiryDate",
                        rejectedValue = exception.absoluteExpiryDate,
                        message = "쿠폰 사용 절대 만료일은 발행 시작일보다 이전일 수 없습니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponPeriodInvalidExpireBeforeIssueEndException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "absoluteExpiryDate",
                        rejectedValue = exception.absoluteExpiryDate,
                        message = "쿠폰 사용 절대 만료일은 발행 종료일보다 이전일 수 없습니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponPeriodInvalidExpireWithoutIssueEndException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "absoluteExpiryDate",
                        rejectedValue = null,
                        message = "쿠폰 사용 절대 만료일은 발행 종료일이 설정된 경우에만 설정할 수 있습니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponPeriodInvalidValidityDaysException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "validityDays",
                        rejectedValue = exception.validityDays,
                        message = "쿠폰 유효 기간은 0보다 커야 합니다."
                    )
                    errors.add(errorDetail)
                }
            }
        }
    }

    private fun addCouponLimitPolicyErrors(
        errors: MutableList<CouponTemplateValidationError>,
        limitPolicy: CouponLimitPolicy,
    ) {
        limitPolicy.validate().onFailure { exception ->
            when (exception) {
                is CouponLimitPolicyInvalidIssueValueException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "maxIssueCount",
                        rejectedValue = exception.maxIssueCount,
                        message = "발급 제한 정책이 설정된 쿠폰은 발급 제한 수량이 필요합니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponLimitPolicyIssueRedeemConflictException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "maxRedeemCount",
                        rejectedValue = exception.maxRedeemCount,
                        message = "발행 제한 정책이 설정된 쿠폰은 사용 제한 수량을 함께 설정할 수 없습니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponLimitPolicyInvalidRedeemValueException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "maxRedeemCount",
                        rejectedValue = limitPolicy.maxRedeemCount,
                        message = "사용 제한 정책이 설정된 쿠폰은 사용 제한 수량이 필요합니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponLimitPolicyRedeemIssueConflictException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "maxIssueCount",
                        rejectedValue = limitPolicy.maxIssueCount,
                        message = "사용 제한 정책이 설정된 쿠폰은 발급 제한 수량을 함께 설정할 수 없습니다."
                    )
                    errors.add(errorDetail)
                }

                is CouponLimitPolicyNoneConflictException -> {
                    val errorDetail = CouponTemplateValidationError(
                        field = "limitType",
                        rejectedValue = limitPolicy.limitType,
                        message = "무제한 정책이 설정된 쿠폰은 발급 및 사용 제한 수량을 설정할 수 없습니다."
                    )
                    errors.add(errorDetail)
                }
            }
        }
    }

    fun reconstruct(
        id: Long,
        merchantId: Long,
        name: String,
        description: String,
        minOrderAmount: Int?,
        discountType: CouponDiscountType,
        discountValue: Int,
        maxDiscountAmount: Int?,
        issueStartAt: LocalDateTime,
        issueEndAt: LocalDateTime?,
        validityDays: Int?,
        absoluteExpiresAt: LocalDateTime?,
        limitType: CouponLimitPolicyType,
        maxIssueCount: Long?,
        maxRedeemCount: Long?,
        status: CouponTemplateStatus,
    ): CouponTemplate {
        return CouponTemplate(
            id = id,
            merchantId = merchantId,
            name = name,
            description = description,
            redemptionCondition = CouponRedemptionCondition(minOrderAmount),
            discountPolicy = CouponDiscountPolicy(discountType, discountValue, maxDiscountAmount),
            period = CouponPeriod(issueStartAt, issueEndAt, validityDays, absoluteExpiresAt),
            limitPolicy = CouponLimitPolicy(
                limitType = limitType,
                maxIssueCount = maxIssueCount,
                maxRedeemCount = maxRedeemCount
            ),
            status = status,
        )
    }
}
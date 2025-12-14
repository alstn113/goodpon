package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException

class CouponLimitPolicyRedeemIssueConflictException(
    val maxIssueCount: Long?,
    cause: Throwable? = null,
) : BaseException("사용 제한 정책이 설정된 쿠폰은 발급 제한 수량을 함께 설정할 수 없습니다.", cause)
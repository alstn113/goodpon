package com.goodpon.core.domain.coupon.template.exception

import com.goodpon.core.support.error.BaseException

class CouponLimitPolicyIssueRedeemConflictException(
    cause: Throwable? = null,
) : BaseException("발급 제한 정책이 설정된 쿠폰은 사용 제한 수량을 함께 설정할 수 없습니다.", cause)
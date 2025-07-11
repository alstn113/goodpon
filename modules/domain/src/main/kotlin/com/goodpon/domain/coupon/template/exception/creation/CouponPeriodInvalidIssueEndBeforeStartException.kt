package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException
import java.time.LocalDate

class CouponPeriodInvalidIssueEndBeforeStartException(
    val issueEndDate: LocalDate,
    cause: Throwable? = null,
) : BaseException("발급 종료일은 발급 시작일 이전일 수 없습니다.", cause)
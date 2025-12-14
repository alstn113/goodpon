package com.goodpon.domain.coupon.template.exception.creation

import com.goodpon.domain.BaseException
import java.time.LocalDate

class CouponPeriodInvalidExpireBeforeStartException(
    val absoluteExpiryDate: LocalDate?,
    cause: Throwable? = null,
) : BaseException("쿠폰 사용 절대 만료일은 발급 시작일보다 이전일 수 없습니다", cause)
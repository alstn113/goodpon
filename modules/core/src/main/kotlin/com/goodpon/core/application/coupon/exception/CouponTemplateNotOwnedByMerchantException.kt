package com.goodpon.core.application.coupon.exception

import com.goodpon.core.support.error.BaseException

class CouponTemplateNotOwnedByMerchantException(
    cause: Throwable? = null,
) : BaseException("가맹점이 소유한 쿠폰 템플릿이 아닙니다.", cause)
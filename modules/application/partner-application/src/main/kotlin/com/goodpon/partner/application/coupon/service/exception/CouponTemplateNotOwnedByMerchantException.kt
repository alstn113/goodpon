package com.goodpon.partner.application.coupon.service.exception

import com.goodpon.domain.BaseException

class CouponTemplateNotOwnedByMerchantException(
    cause: Throwable? = null,
) : BaseException("상점이 소유한 쿠폰 템플릿이 아닙니다.", cause)
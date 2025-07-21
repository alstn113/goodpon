package com.goodpon.application.partner.merchant.port.out.exception

import com.goodpon.domain.BaseException

class MerchantNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 상점입니다.", cause)
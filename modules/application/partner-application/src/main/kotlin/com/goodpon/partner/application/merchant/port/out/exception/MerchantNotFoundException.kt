package com.goodpon.partner.application.merchant.port.out.exception

import com.goodpon.domain.support.error.BaseException

class MerchantNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 가맹점입니다.", cause)
package com.goodpon.dashboard.application.merchant.port.out.exception

import com.goodpon.domain.BaseException

class UnauthorizedMerchantAccountException(
    cause: Throwable? = null,
) : BaseException("가맹점에 속하지 않은 계정입니다.", cause)

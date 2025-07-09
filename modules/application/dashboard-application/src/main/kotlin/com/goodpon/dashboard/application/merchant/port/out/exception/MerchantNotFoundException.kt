package com.goodpon.dashboard.application.merchant.port.out.exception

import com.goodpon.domain.BaseException

class MerchantNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 가맹점입니다.", cause)
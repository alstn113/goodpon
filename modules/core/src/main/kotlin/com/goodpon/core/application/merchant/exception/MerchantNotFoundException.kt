package com.goodpon.core.application.merchant.exception

import com.goodpon.core.support.error.BaseException

class MerchantNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 가맹점입니다.", cause)
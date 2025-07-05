package com.goodpon.core.domain.merchant.exception

import com.goodpon.core.support.error.BaseException

class MerchantAccountNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 가맹점 계정입니다.", cause)
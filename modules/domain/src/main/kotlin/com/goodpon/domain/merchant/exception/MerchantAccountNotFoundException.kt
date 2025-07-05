package com.goodpon.domain.merchant.exception

import com.goodpon.domain.support.error.BaseException

class MerchantAccountNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 가맹점 계정입니다.", cause)
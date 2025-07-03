package com.goodpon.core.domain.account.exception

import com.goodpon.core.support.error.BaseException

class AccountInvalidNameLengthException(
    cause: Throwable? = null,
) : BaseException("계정 이름은 50자 이하여야 합니다.", cause)
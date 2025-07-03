package com.goodpon.core.domain.account.exception

import com.goodpon.core.support.error.BaseException

class AccountInvalidEmailFormatException(
    cause: Throwable? = null,
) : BaseException("올바르지 않은 계정 이메일 형식입니다.", cause)
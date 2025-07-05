package com.goodpon.domain.account.exception

import com.goodpon.domain.support.error.BaseException

class AccountInvalidEmailFormatException(
    cause: Throwable? = null,
) : BaseException("올바르지 않은 계정 이메일 형식입니다.", cause)
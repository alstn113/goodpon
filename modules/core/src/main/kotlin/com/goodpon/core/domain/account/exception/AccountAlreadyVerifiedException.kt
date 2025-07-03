package com.goodpon.core.domain.account.exception

import com.goodpon.core.support.error.BaseException

class AccountAlreadyVerifiedException(
    cause: Throwable? = null,
) : BaseException("이미 인증된 계정입니다.", cause)
package com.goodpon.domain.account.exception

import com.goodpon.domain.BaseException

class AccountAlreadyVerifiedException(
    cause: Throwable? = null,
) : BaseException("이미 인증된 계정입니다.", cause)
package com.goodpon.domain.account.exception

import com.goodpon.domain.BaseException

class AccountInvalidPasswordLengthException(
    cause: Throwable? = null,
) : BaseException("계정 비밀번호는 8자 이상 100자 이하여야 합니다.", cause)
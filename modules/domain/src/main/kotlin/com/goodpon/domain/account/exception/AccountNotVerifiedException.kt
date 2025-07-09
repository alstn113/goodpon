package com.goodpon.domain.account.exception

import com.goodpon.domain.BaseException

class AccountNotVerifiedException(
    cause: Throwable? = null,
) : BaseException("이메일 인증이 완료되지 않았습니다.", cause)
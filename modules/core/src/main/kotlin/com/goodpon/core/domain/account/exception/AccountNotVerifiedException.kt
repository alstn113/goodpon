package com.goodpon.core.domain.account.exception

import com.goodpon.core.support.error.BaseException

class AccountNotVerifiedException(
    cause: Throwable? = null,
) : BaseException("이메일 인증이 완료되지 않았습니다.", cause)
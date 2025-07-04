package com.goodpon.core.application.auth.exception

import com.goodpon.core.support.error.BaseException

class PasswordMismatchException(
    cause: Throwable? = null,
) : BaseException("비밀번호가 일치하지 않습니다.", cause)
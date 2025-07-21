package com.goodpon.application.dashboard.auth.service.exception

import com.goodpon.domain.BaseException

class PasswordMismatchException(
    cause: Throwable? = null,
) : BaseException("비밀번호가 일치하지 않습니다.", cause)
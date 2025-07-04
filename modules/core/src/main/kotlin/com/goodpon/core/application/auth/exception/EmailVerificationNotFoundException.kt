package com.goodpon.core.application.auth.exception

import com.goodpon.core.support.error.BaseException

class EmailVerificationNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 이메일 인증입니다.", cause)
package com.goodpon.domain.auth.exception

import com.goodpon.domain.support.error.BaseException

class EmailVerificationNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 이메일 인증입니다.", cause)
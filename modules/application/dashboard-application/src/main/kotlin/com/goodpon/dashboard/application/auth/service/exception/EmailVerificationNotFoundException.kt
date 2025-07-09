package com.goodpon.dashboard.application.auth.service.exception

import com.goodpon.domain.BaseException

class EmailVerificationNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 이메일 인증입니다.", cause)
package com.goodpon.dashboard.application.account.service.exception

import com.goodpon.domain.support.error.BaseException

class AccountEmailExistsException(
    cause: Throwable? = null,
) : BaseException("이미 존재하는 계정 이메일입니다.", cause)
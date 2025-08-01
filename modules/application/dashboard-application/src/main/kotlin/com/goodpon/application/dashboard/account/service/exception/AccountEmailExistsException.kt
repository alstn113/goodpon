package com.goodpon.application.dashboard.account.service.exception

import com.goodpon.domain.BaseException

class AccountEmailExistsException(
    cause: Throwable? = null,
) : BaseException("이미 존재하는 계정 이메일입니다.", cause)
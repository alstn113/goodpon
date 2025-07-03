package com.goodpon.core.domain.account.exception

import com.goodpon.core.support.error.BaseException

class AccountEmailExistsException(
    cause: Throwable? = null,
) : BaseException("이미 존재하는 계정 이메일입니다.", cause)
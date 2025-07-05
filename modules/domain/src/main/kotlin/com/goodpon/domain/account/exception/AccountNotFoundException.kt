package com.goodpon.domain.account.exception

import com.goodpon.domain.support.error.BaseException

class AccountNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 계정입니다.", cause)
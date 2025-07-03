package com.goodpon.core.domain.account.exception

import com.goodpon.core.support.error.BaseException

class AccountNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 계정입니다.", cause)
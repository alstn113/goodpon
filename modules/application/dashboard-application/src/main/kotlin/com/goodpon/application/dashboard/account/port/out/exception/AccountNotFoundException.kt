package com.goodpon.application.dashboard.account.port.out.exception

import com.goodpon.domain.BaseException

class AccountNotFoundException(
    cause: Throwable? = null,
) : BaseException("존재하지 않는 계정입니다.", cause)
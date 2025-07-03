package com.goodpon.core.domain.account.exception

import com.goodpon.core.support.error.BaseException

class AccountNameBlankException(
    cause: Throwable? = null,
) : BaseException("계정 이름은 공백으로만 이루어질 수 없습니다.", cause)
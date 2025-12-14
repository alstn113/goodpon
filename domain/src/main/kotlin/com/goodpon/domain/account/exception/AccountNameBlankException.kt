package com.goodpon.domain.account.exception

import com.goodpon.domain.BaseException

class AccountNameBlankException(
    cause: Throwable? = null,
) : BaseException("계정 이름은 공백으로만 이루어질 수 없습니다.", cause)
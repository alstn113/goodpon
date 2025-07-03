package com.goodpon.core.domain.account.vo

import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType

@JvmInline
value class AccountPassword(val value: String) {
    init {
        if (value.length !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH) {
            throw CoreException(ErrorType.INVALID_ACCOUNT_PASSWORD_LENGTH)
        }
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private const val PASSWORD_MAX_LENGTH = 100
    }
}
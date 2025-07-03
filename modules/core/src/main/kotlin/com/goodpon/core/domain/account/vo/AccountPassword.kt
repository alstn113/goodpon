package com.goodpon.core.domain.account.vo

import com.goodpon.core.domain.account.exception.AccountInvalidPasswordLengthException

@JvmInline
value class AccountPassword(val value: String) {
    init {
        if (value.length !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH) {
            throw AccountInvalidPasswordLengthException()
        }
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private const val PASSWORD_MAX_LENGTH = 100
    }
}
package com.goodpon.core.domain.account.vo

import com.goodpon.core.domain.account.exception.AccountInvalidNameLengthException
import com.goodpon.core.domain.account.exception.AccountNameBlankException

@JvmInline
value class AccountName(val value: String) {
    init {
        if (value.isBlank()) {
            throw AccountNameBlankException()
        }
        if (value.length > MAX_LENGTH) {
            throw AccountInvalidNameLengthException()
        }
    }

    companion object {
        private const val MAX_LENGTH = 50
    }
}
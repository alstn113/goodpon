package com.goodpon.core.domain.account.vo

import com.goodpon.core.support.error.CoreException
import com.goodpon.core.support.error.ErrorType

@JvmInline
value class AccountName(val value: String) {
    init {
        if (value.isBlank()) {
            throw CoreException(ErrorType.ACCOUNT_NAME_NOT_BLANK)
        }
        if (value.length > MAX_LENGTH) {
            throw CoreException(ErrorType.INVALID_ACCOUNT_NAME_LENGTH)
        }
    }

    companion object {
        private const val MAX_LENGTH = 50
    }
}
package com.goodpon.core.domain.account.vo

import com.goodpon.core.domain.account.exception.AccountInvalidEmailFormatException

@JvmInline
value class AccountEmail(val value: String) {
    init {
        if (!EMAIL_REGEX.matches(value)) {
            throw AccountInvalidEmailFormatException()
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    }
}

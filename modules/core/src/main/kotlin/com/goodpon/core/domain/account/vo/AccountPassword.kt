package com.goodpon.core.domain.account.vo

data class AccountPassword(val value: String) {
    init {
        if (value.length < PASSWORD_MIN_LENGTH) {
            throw IllegalArgumentException("Password must be at least $PASSWORD_MIN_LENGTH characters long")
        }
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }
}
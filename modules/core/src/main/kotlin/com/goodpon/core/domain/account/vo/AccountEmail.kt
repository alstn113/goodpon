package com.goodpon.core.domain.account.vo

data class AccountEmail(val value: String) {
    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }

    init {
        if (!EMAIL_REGEX.matches(value)) {
            throw IllegalArgumentException("Invalid email format: $value")
        }
    }
}

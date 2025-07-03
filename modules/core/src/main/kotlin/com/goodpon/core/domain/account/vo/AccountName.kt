package com.goodpon.core.domain.account.vo

data class AccountName(val value: String) {
    init {
        if (value.isBlank()) {
            throw IllegalArgumentException("Account name cannot be blank")
        }
        if (value.length > MAX_LENGTH) {
            throw IllegalArgumentException("Account name cannot exceed $MAX_LENGTH characters")
        }
    }

    companion object {
        private const val MAX_LENGTH = 50
    }
}
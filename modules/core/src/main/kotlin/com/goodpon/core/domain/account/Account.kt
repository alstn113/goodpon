package com.goodpon.core.domain.account

import java.time.LocalDateTime

data class Account(
    val id: Long,
    val email: String,
    val password: String,
    val name: String,
    val verified: Boolean = false,
    val verifiedAt: LocalDateTime? = null,
) {

    fun verify(): Account {
        if (verified) {
            throw IllegalStateException("Account is already verified")
        }

        return this.copy(
            verified = true,
            verifiedAt = LocalDateTime.now()
        )
    }

    companion object {

        fun create(email: String, password: String, name: String): Account {
            return Account(
                id = 0,
                email = email,
                password = password,
                name = name,
            )
        }
    }
}
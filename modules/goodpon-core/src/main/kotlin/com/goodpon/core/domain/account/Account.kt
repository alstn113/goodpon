package com.goodpon.core.domain.account

data class Account(
    val id: Long,
    val email: String,
    val password: String,
    val name: String,
    val status: AccountStatus,
) {

    fun verify(): Account {
        return copy(status = AccountStatus.VERIFIED)
    }

    fun isNotVerified(): Boolean {
        return status != AccountStatus.VERIFIED
    }

    fun isNotUnverified(): Boolean {
        return status != AccountStatus.UNVERIFIED
    }

    fun isUnverified(): Boolean {
        return status == AccountStatus.UNVERIFIED
    }

    companion object {

        fun create(email: String, password: String, name: String): Account {
            return Account(
                id = 0,
                email = email,
                password = password,
                name = name,
                status = AccountStatus.UNVERIFIED
            )
        }
    }
}
package com.goodpon.common.domain.account

data class Account(
    val id: Long,
    val email: String,
    val password: String,
    val name: String,
    val status: AccountStatus,
) {

    companion object {

        fun create(email: String, password: String, name: String): Account {
            return Account(
                id = 0,
                email = email,
                password = password,
                name = name,
                status = AccountStatus.PENDING
            )
        }
    }

    fun activate(): Account {
        return copy(status = AccountStatus.ACTIVE)
    }

    fun isNotPending(): Boolean {
        return status != AccountStatus.PENDING
    }

    fun isPending(): Boolean {
        return status == AccountStatus.PENDING
    }
}
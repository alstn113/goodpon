package io.github.alstn113.goodpon.domain.account

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

    fun isActive(): Boolean {
        return status == AccountStatus.ACTIVE
    }

    fun isBlocked(): Boolean {
        return status == AccountStatus.BLOCKED
    }

    fun isPending(): Boolean {
        return status == AccountStatus.PENDING
    }
}
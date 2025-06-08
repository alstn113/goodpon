package io.github.alstn113.goodpon.domain.account

interface AccountRepository {

    fun save(account: Account): Account
    fun findByEmail(email: String): Account?
}

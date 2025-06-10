package com.goodpon.common.domain.account

interface AccountRepository {

    fun save(account: Account): Account
    fun findById(id: Long): Account?
    fun findByEmail(email: String): Account?
    fun existsByEmail(email: String): Boolean
}

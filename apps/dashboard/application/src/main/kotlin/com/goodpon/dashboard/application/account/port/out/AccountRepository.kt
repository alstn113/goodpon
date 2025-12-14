package com.goodpon.dashboard.application.account.port.out

import com.goodpon.domain.account.Account

interface AccountRepository {

    fun save(account: Account): Account

    fun findById(id: Long): Account?

    fun findByEmail(email: String): Account?

    fun existsByEmail(email: String): Boolean
}
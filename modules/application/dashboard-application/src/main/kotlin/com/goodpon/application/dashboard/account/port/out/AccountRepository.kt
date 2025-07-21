package com.goodpon.application.dashboard.account.port.out

import com.goodpon.domain.account.Account

interface AccountRepository {

    fun save(account: Account): Account

    fun findById(id: Long): Account?

    fun findByEmail(email: String): Account?

    fun existsByEmail(email: String): Boolean
}
package com.goodpon.infra.jpa.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {

    fun findByEmail(email: String): AccountEntity?

    fun existsByEmail(email: String): Boolean
}

package com.goodpon.infra.jpa.repository

import com.goodpon.infra.jpa.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {

    fun findByEmail(email: String): AccountEntity?

    fun existsByEmail(email: String): Boolean
}

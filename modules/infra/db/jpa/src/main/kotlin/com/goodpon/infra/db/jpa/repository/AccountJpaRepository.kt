package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {

    fun findByEmail(email: String): AccountEntity?

    fun existsByEmail(email: String): Boolean
}

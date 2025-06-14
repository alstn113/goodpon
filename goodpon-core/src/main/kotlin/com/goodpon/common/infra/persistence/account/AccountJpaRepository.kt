package com.goodpon.common.infra.persistence.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {

    fun findByEmail(email: String): AccountEntity?
    fun existsByEmail(email: String): Boolean
}

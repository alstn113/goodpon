package io.github.alstn113.goodpon.infra.persistence.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountJpaRepository : JpaRepository<AccountEntity, Long>
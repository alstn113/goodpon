package io.github.alstn113.goodpon.infra.persistence.transaction

import org.springframework.data.jpa.repository.JpaRepository

interface TransactionJpaRepository : JpaRepository<TransactionEntity, String>
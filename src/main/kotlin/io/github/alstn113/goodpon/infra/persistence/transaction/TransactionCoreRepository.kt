package io.github.alstn113.goodpon.infra.persistence.transaction

import io.github.alstn113.goodpon.domain.transaction.TransactionRepository
import org.springframework.stereotype.Repository

@Repository
class TransactionCoreRepository(
    private val transactionJpaRepository: TransactionJpaRepository,
) : TransactionRepository
package io.github.alstn113.payments.infra.persistence.transaction

import io.github.alstn113.payments.domain.transaction.TransactionRepository
import org.springframework.stereotype.Repository

@Repository
class TransactionCoreRepository(
    private val transactionJpaRepository: TransactionJpaRepository,
) : TransactionRepository
package io.github.alstn113.payments.infra.persistence.account

import io.github.alstn113.payments.domain.account.AccountRepository
import org.springframework.stereotype.Repository

@Repository
class AccountCoreRepository(
    private val accountJpaRepository: AccountJpaRepository,
) : AccountRepository
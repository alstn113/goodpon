package io.github.alstn113.goodpon.infra.persistence.account

import io.github.alstn113.goodpon.domain.account.AccountRepository
import org.springframework.stereotype.Repository

@Repository
class AccountCoreRepository(
    private val accountJpaRepository: AccountJpaRepository,
) : AccountRepository
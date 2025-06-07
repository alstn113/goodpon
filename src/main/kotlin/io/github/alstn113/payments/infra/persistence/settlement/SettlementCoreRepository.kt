package io.github.alstn113.payments.infra.persistence.settlement

import io.github.alstn113.payments.domain.settlement.SettlementRepository
import org.springframework.stereotype.Repository

@Repository
class SettlementCoreRepository(
    private val settlementJpaRepository: SettlementJpaRepository,
) : SettlementRepository

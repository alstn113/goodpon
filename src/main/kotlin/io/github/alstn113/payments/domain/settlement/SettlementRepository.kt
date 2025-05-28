package io.github.alstn113.payments.domain.settlement

import org.springframework.data.jpa.repository.JpaRepository

interface SettlementRepository : JpaRepository<Settlement, String>
package io.github.alstn113.payments.infra.persistence.settlement

import org.springframework.data.jpa.repository.JpaRepository

interface SettlementJpaRepository : JpaRepository<SettlementEntity, String>
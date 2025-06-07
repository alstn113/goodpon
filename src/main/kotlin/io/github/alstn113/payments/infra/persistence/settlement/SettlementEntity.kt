package io.github.alstn113.payments.infra.persistence.settlement

import io.github.alstn113.payments.domain.settlement.Settlement
import io.github.alstn113.payments.infra.persistence.AuditableEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "settlements")
class SettlementEntity : AuditableEntity() {

    @Id
    @Tsid
    val id: String? = null

    fun toSettlement(): Settlement {
        return Settlement(
            id = id!!,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
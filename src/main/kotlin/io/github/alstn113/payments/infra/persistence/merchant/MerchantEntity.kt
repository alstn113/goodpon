package io.github.alstn113.payments.infra.persistence.merchant

import io.github.alstn113.payments.domain.merchant.Merchant
import io.github.alstn113.payments.infra.persistence.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "merchants")
class MerchantEntity(

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val businessNumber: String,

    @Column(nullable = false, unique = true)
    val clientKey: String,

    @Column(nullable = false, unique = true)
    val secretKey: String,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun toMerchant(): Merchant {
        return Merchant(
            id = id,
            name = name,
            businessNumber = businessNumber,
            clientKey = clientKey,
            secretKey = secretKey
        )
    }
}
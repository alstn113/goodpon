package com.goodpon.infra.persistence.merchant

import com.goodpon.goodpon.domain.merchant.Merchant
import com.goodpon.goodpon.infra.persistence.AuditableEntity
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
            secretKey = secretKey,
        )
    }
}

package com.goodpon.common.infra.persistence.merchant

import com.goodpon.common.domain.merchant.Merchant
import com.goodpon.common.infra.persistence.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "merchants")
class MerchantEntity(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val businessNumber: String,

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
            secretKey = secretKey,
        )
    }
}

package com.goodpon.infra.db.merchant

import com.goodpon.core.domain.merchant.Merchant
import jakarta.persistence.*

@Entity
@Table(name = "merchants")
class MerchantEntity(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val secretKey: String,
) : com.goodpon.infra.db.AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    companion object {
        fun fromDomain(merchant: Merchant): MerchantEntity {
            return MerchantEntity(
                name = merchant.name,
                secretKey = merchant.secretKey
            )
        }
    }

    fun toDomain(): Merchant {
        return Merchant(
            id = id,
            name = name,
            secretKey = secretKey,
        )
    }

    fun update(merchant: Merchant) {
    }
}

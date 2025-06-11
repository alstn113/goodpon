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
    val secretKey: String,
) : AuditableEntity() {
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
}

package com.goodpon.infra.jpa.merchant

import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.jpa.AuditableEntity
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

    fun update(merchant: Merchant) {}

    fun toDomain(): Merchant {
        return Merchant.reconstruct(
            id = id,
            name = name,
            secretKey = secretKey,
        )
    }

    companion object {
        fun fromDomain(merchant: Merchant): MerchantEntity {
            return MerchantEntity(
                name = merchant.name,
                secretKey = merchant.secretKey
            )
        }
    }
}

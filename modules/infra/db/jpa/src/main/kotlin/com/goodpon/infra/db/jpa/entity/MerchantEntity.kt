package com.goodpon.infra.db.jpa.entity

import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.domain.merchant.MerchantClientSecret
import jakarta.persistence.*

@Entity
@Table(name = "merchants")
class MerchantEntity(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, columnDefinition = "VARCHAR(35)")
    val clientId: String,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun update(merchant: Merchant) {}

    fun toDomain(secrets: List<MerchantClientSecret>, accounts: List<MerchantAccount>): Merchant {
        return Merchant.reconstruct(
            id = id,
            name = name,
            clientId = clientId,
            secrets = secrets,
            accounts = accounts
        )
    }

    companion object {
        fun fromDomain(merchant: Merchant): MerchantEntity {
            return MerchantEntity(
                name = merchant.name,
                clientId = merchant.clientId
            )
        }
    }
}

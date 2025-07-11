package com.goodpon.infra.db.jpa.entity

import com.goodpon.domain.merchant.MerchantClientSecret
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "merchant_client_secrets")
class MerchantClientSecretEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: MerchantEntity? = null,

    @Column(nullable = false)
    val key: String,

    @Column(nullable = false)
    var expiredAt: LocalDateTime?,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun update(merchantClientSecret: MerchantClientSecret) {
        this.expiredAt = merchantClientSecret.expiredAt
    }

    fun toDomain(): MerchantClientSecret {
        return MerchantClientSecret.reconstruct(
            id = id,
            key = key,
            expiredAt = expiredAt
        )
    }

    companion object {
        fun fromDomain(domain: MerchantClientSecret, merchantEntity: MerchantEntity): MerchantClientSecretEntity {
            return MerchantClientSecretEntity(
                key = domain.key,
                expiredAt = domain.expiredAt,
                merchant = merchantEntity
            )
        }
    }
}
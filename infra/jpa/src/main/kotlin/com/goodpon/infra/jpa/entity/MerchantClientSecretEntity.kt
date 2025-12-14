package com.goodpon.infra.jpa.entity

import com.goodpon.domain.merchant.MerchantClientSecret
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "merchant_client_secrets")
class MerchantClientSecretEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: MerchantEntity? = null,

    @Column(nullable = false, columnDefinition = "VARCHAR(35)")
    val secret: String,

    @Column
    var expiredAt: LocalDateTime?,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun update(merchantClientSecret: MerchantClientSecret) {
        this.expiredAt = merchantClientSecret.expiredAt
    }

    fun toDomain(): MerchantClientSecret {
        return MerchantClientSecret.reconstruct(
            id = id,
            secret = secret,
            expiredAt = expiredAt
        )
    }

    companion object {
        fun fromDomain(domain: MerchantClientSecret, merchantEntity: MerchantEntity): MerchantClientSecretEntity {
            return MerchantClientSecretEntity(
                secret = domain.secret,
                expiredAt = domain.expiredAt,
                merchant = merchantEntity
            )
        }
    }
}
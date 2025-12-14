package com.goodpon.infra.jpa.entity

import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.domain.merchant.MerchantAccountRole
import jakarta.persistence.*

@Entity
@Table(name = "merchant_accounts")
class MerchantAccountEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: MerchantEntity? = null,

    @Column(nullable = false)
    val accountId: Long,

    @Column(nullable = false, columnDefinition = "VARCHAR(25)")
    @Enumerated(EnumType.STRING)
    val role: MerchantAccountRole,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun update(merchantAccount: MerchantAccount) {}

    fun toDomain(): MerchantAccount {
        return MerchantAccount.reconstruct(
            id = id,
            accountId = accountId,
            role = role,
        )
    }

    companion object {
        fun fromDomain(merchantAccount: MerchantAccount, merchantEntity: MerchantEntity): MerchantAccountEntity {
            return MerchantAccountEntity(
                accountId = merchantAccount.accountId,
                role = merchantAccount.role,
                merchant = merchantEntity
            )
        }
    }
}

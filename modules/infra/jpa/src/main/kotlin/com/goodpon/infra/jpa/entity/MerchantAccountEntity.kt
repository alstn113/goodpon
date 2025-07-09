package com.goodpon.infra.jpa.entity

import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.domain.merchant.MerchantAccountRole
import jakarta.persistence.*

@Entity
@Table(name = "merchant_accounts")
class MerchantAccountEntity(
    @Column(nullable = false)
    val merchantId: Long,

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
            merchantId = merchantId,
            accountId = accountId,
            role = role,
        )
    }

    companion object {
        fun fromDomain(merchantAccount: MerchantAccount): MerchantAccountEntity {
            return MerchantAccountEntity(
                merchantId = merchantAccount.merchantId,
                accountId = merchantAccount.accountId,
                role = merchantAccount.role,
            )
        }
    }
}

package com.goodpon.infra.jpa.merchant

import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.domain.domain.merchant.MerchantAccountRole
import com.goodpon.infra.jpa.AuditableEntity
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

    fun toDomain(): com.goodpon.domain.merchant.MerchantAccount {
        return com.goodpon.domain.merchant.MerchantAccount.reconstruct(
            id = id,
            merchantId = merchantId,
            accountId = accountId,
            role = role,
        )
    }

    fun update(merchantAccount: com.goodpon.domain.merchant.MerchantAccount) {
    }

    companion object {
        fun fromDomain(merchantAccount: com.goodpon.domain.merchant.MerchantAccount): MerchantAccountEntity {
            return MerchantAccountEntity(
                merchantId = merchantAccount.merchantId,
                accountId = merchantAccount.accountId,
                role = merchantAccount.role,
            )
        }
    }
}

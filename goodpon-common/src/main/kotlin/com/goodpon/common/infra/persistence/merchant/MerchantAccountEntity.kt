package com.goodpon.common.infra.persistence.merchant

import com.goodpon.common.domain.merchant.MerchantAccount
import com.goodpon.common.domain.merchant.MerchantAccountRole
import com.goodpon.common.infra.persistence.AuditableEntity
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

    companion object {
        fun fromDomain(merchantAccount: MerchantAccount): MerchantAccountEntity {
            return MerchantAccountEntity(
                merchantId = merchantAccount.merchantId,
                accountId = merchantAccount.accountId,
                role = merchantAccount.role,
            )
        }
    }

    fun toDomain(): MerchantAccount {
        return MerchantAccount(
            id = id,
            merchantId = merchantId,
            accountId = accountId,
            role = role,
        )
    }
}

package com.goodpon.infra.db.merchant

import com.goodpon.core.domain.merchant.MerchantAccount
import com.goodpon.core.domain.merchant.MerchantAccountRole
import com.goodpon.infra.db.AuditableEntity
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "merchant_accounts")
@EntityListeners(AuditingEntityListener::class)
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

    fun toDomain(): MerchantAccount {
        return MerchantAccount(
            id = id,
            merchantId = merchantId,
            accountId = accountId,
            role = role,
        )
    }

    fun update(merchantAccount: MerchantAccount) {
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

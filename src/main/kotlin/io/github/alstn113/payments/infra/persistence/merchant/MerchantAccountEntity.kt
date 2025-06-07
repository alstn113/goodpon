package io.github.alstn113.payments.infra.persistence.merchant

import io.github.alstn113.payments.domain.merchant.MerchantAccount
import io.github.alstn113.payments.domain.merchant.MerchantAccountRole
import io.github.alstn113.payments.infra.persistence.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "merchant_accounts")
class MerchantAccountEntity(
    @Column(nullable = false)
    val mId: Long,

    @Column(nullable = false)
    val accountId: Long,

    @Column(nullable = false, columnDefinition = "VARCHAR(25)")
    @Enumerated(EnumType.STRING)
    val role: MerchantAccountRole,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun toMerchantAccount(): MerchantAccount {
        return MerchantAccount(
            id = id,
            mid = mId,
            accountId = accountId,
            role = role
        )
    }
}
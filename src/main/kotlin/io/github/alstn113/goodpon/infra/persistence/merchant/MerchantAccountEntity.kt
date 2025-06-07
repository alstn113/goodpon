package io.github.alstn113.goodpon.infra.persistence.merchant

import io.github.alstn113.goodpon.domain.merchant.MerchantAccount
import io.github.alstn113.goodpon.domain.merchant.MerchantAccountRole
import io.github.alstn113.goodpon.infra.persistence.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "merchant_accounts")
class MerchantAccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val mId: Long,

    @Column(nullable = false)
    val accountId: Long,

    @Column(nullable = false, columnDefinition = "VARCHAR(25)")
    @Enumerated(EnumType.STRING)
    val role: MerchantAccountRole,
) : AuditableEntity() {

    fun toMerchantAccount(): MerchantAccount {
        return MerchantAccount(
            id = id,
            mid = mId,
            accountId = accountId,
            role = role
        )
    }
}
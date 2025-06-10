package com.goodpon.common.infra.persistence.merchant

import com.goodpon.common.domain.merchant.MerchantAccount
import com.goodpon.common.domain.merchant.MerchantAccountRole
import com.goodpon.common.infra.persistence.AuditableEntity
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
            role = role,
        )
    }
}

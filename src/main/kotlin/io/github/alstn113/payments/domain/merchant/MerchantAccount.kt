package io.github.alstn113.payments.domain.merchant

import io.github.alstn113.payments.domain.Timestamps
import jakarta.persistence.*

@Entity
@Table(name = "merchant_accounts")
class MerchantAccount(
    @Column(nullable = false)
    val mid: Long,

    @Column(nullable = false)
    val accountId: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val merchantAccountRole: MerchantAccountRole,

    @Embedded
    private val timestamps: Timestamps = Timestamps()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
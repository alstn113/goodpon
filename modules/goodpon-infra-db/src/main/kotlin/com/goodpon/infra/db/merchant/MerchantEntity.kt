package com.goodpon.infra.db.merchant

import com.goodpon.core.domain.merchant.Merchant
import com.goodpon.infra.db.AuditableEntity
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "merchants")
@EntityListeners(AuditingEntityListener::class)
class MerchantEntity(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val secretKey: String,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun toDomain(): Merchant {
        return Merchant(
            id = id,
            name = name,
            secretKey = secretKey,
        )
    }

    fun update(merchant: Merchant) {
    }

    companion object {
        fun fromDomain(merchant: Merchant): MerchantEntity {
            return MerchantEntity(
                name = merchant.name,
                secretKey = merchant.secretKey
            )
        }
    }
}

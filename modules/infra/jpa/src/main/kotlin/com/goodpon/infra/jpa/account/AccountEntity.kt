package com.goodpon.infra.jpa.account

import com.goodpon.core.domain.account.Account
import com.goodpon.infra.jpa.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "accounts")
class AccountEntity(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val verified: Boolean,

    @Column
    val verifiedAt: LocalDateTime?,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun toDomain(): Account {
        return Account(
            id = id,
            email = email,
            password = password,
            name = name,
            verified = verified,
            verifiedAt = verifiedAt
        )
    }

    fun update(account: Account) {
    }

    companion object {
        fun fromDomain(account: Account): AccountEntity {
            return AccountEntity(
                email = account.email,
                password = account.password,
                name = account.name,
                verified = account.verified,
                verifiedAt = account.verifiedAt
            )
        }
    }
}

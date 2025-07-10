package com.goodpon.infra.db.jpa.entity

import com.goodpon.domain.account.Account
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
    var verified: Boolean,

    @Column
    var verifiedAt: LocalDateTime?,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun update(account: Account) {
        this.verified = account.verified
        this.verifiedAt = account.verifiedAt
    }

    fun toDomain(): Account {
        return Account.reconstruct(
            id = id,
            email = email,
            password = password,
            name = name,
            verified = verified,
            verifiedAt = verifiedAt
        )
    }

    companion object {
        fun fromDomain(account: Account): AccountEntity {
            return AccountEntity(
                email = account.email.value,
                password = account.password.value,
                name = account.name.value,
                verified = account.verified,
                verifiedAt = account.verifiedAt
            )
        }
    }
}

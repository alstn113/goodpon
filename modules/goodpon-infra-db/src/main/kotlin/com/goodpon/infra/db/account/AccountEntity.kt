package com.goodpon.infra.db.account

import com.goodpon.core.domain.account.Account
import com.goodpon.core.domain.account.AccountStatus
import com.goodpon.infra.db.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "accounts")
class AccountEntity(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: AccountStatus,
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
            status = status
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
                status = account.status,
            )
        }
    }
}

package io.github.alstn113.goodpon.infra.persistence.account

import io.github.alstn113.goodpon.domain.account.Account
import io.github.alstn113.goodpon.domain.account.AccountStatus
import io.github.alstn113.goodpon.infra.persistence.AuditableEntity
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

    companion object {
        fun fromDomain(account: Account): AccountEntity {
            return AccountEntity(
                email = account.email,
                password = account.password,
                name = account.name,
                status = account.status
            )
        }
    }

    fun toDomain(): Account {
        return Account(
            id = id,
            email = email,
            password = password,
            name = name,
            status = status
        )
    }
}

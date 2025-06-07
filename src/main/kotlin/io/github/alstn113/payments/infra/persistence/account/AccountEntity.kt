package io.github.alstn113.payments.infra.persistence.account

import io.github.alstn113.payments.domain.account.Account
import io.github.alstn113.payments.infra.persistence.AuditableEntity
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
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun toAccount(): Account {
        return Account(
            id = id,
            email = email,
            password = password,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
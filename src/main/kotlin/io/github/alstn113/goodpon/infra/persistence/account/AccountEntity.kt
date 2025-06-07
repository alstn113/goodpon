package io.github.alstn113.goodpon.infra.persistence.account

import io.github.alstn113.goodpon.domain.account.Account
import io.github.alstn113.goodpon.infra.persistence.AuditableEntity
import jakarta.persistence.*

@Entity
@Table(name = "accounts")
class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val name: String,
) : AuditableEntity() {

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
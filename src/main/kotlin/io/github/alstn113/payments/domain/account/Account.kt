package io.github.alstn113.payments.domain.account

import io.github.alstn113.payments.domain.Timestamps
import jakarta.persistence.*

@Entity
@Table(name = "accounts")
class Account(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val name: String,

    @Embedded
    val timestamps: Timestamps = Timestamps()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
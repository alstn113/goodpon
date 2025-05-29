package io.github.alstn113.payments.domain.merchant

import io.github.alstn113.payments.domain.Timestamps
import jakarta.persistence.*

@Entity
@Table(name = "merchants")
class Merchant(

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val businessNumber: String,

    @Column(nullable = false, unique = true)
    val clientKey: String,

    @Column(nullable = false, unique = true)
    val secretKey: String,

    @Embedded
    val timestamps: Timestamps = Timestamps()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
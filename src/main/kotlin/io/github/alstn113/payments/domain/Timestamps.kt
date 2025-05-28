package io.github.alstn113.payments.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EntityListeners
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Embeddable
@EntityListeners(AuditingEntityListener::class)
class Timestamps {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.MIN

    @LastModifiedDate
    @Column(nullable = false)
    val updatedAt: Instant = Instant.MIN
}
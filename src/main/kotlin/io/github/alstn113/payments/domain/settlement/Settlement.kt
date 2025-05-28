package io.github.alstn113.payments.domain.settlement

import io.github.alstn113.payments.domain.Timestamps
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "settlements")
class Settlement(

    @Embedded
    val timestamps: Timestamps = Timestamps()
) {

    @Id
    @Tsid
    val id: String? = null
}
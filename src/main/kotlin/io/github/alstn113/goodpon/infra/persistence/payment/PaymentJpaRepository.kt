package io.github.alstn113.goodpon.infra.persistence.payment

import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<PaymentEntity, String>
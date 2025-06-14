package com.goodpon.common.infra.persistence.coupon

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class IssuedCouponEntity {

    @Id
    val id: UUID = UUID.randomUUID()
}
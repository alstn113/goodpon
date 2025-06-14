package com.goodpon.infra.db.coupon

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IssuedCouponJpaRepository : JpaRepository<IssuedCouponEntity, UUID>

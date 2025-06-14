package com.goodpon.common.infra.persistence.coupon

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IssuedCouponJpaRepository : JpaRepository<IssuedCouponEntity, UUID>

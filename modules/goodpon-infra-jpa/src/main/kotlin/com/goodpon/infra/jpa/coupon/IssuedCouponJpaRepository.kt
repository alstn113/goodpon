package com.goodpon.infra.jpa.coupon

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IssuedCouponJpaRepository : JpaRepository<IssuedCouponEntity, UUID> {

    fun findFirstByUserIdAndCouponTemplateId(userId: Long, couponTemplateId: Long): IssuedCouponEntity?
}

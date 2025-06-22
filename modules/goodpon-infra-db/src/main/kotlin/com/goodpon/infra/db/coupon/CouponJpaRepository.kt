package com.goodpon.infra.db.coupon

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CouponJpaRepository : JpaRepository<CouponEntity, UUID> {

    fun findByAccountIdAndCouponTemplateId(accountId: Long, couponTemplateId: Long): MutableList<CouponEntity>
    fun findFirstByAccountIdAndCouponTemplateId(accountId: Long, couponTemplateId: Long): CouponEntity?
}

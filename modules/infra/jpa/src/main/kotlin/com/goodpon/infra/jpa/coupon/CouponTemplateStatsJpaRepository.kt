package com.goodpon.infra.jpa.coupon

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface CouponTemplateStatsJpaRepository : JpaRepository<CouponTemplateStatsEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query("SELECT c FROM CouponTemplateStatsEntity c WHERE c.couponTemplateId = :couponTemplateId")
    fun findByCouponTemplateIdWithLock(couponTemplateId: Long): CouponTemplateStatsEntity?
}
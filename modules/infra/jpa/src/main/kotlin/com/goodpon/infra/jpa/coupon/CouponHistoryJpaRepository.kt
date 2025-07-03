package com.goodpon.infra.jpa.coupon

import org.springframework.data.jpa.repository.JpaRepository

interface CouponHistoryJpaRepository : JpaRepository<CouponHistoryEntity, String> {
    fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistoryEntity>
}
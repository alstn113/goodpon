package com.goodpon.infra.jpa.coupon.repository

import com.goodpon.infra.jpa.coupon.entity.CouponHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponHistoryJpaRepository : JpaRepository<CouponHistoryEntity, String> {

    fun findByUserCouponIdOrderByRecordedAtDesc(userCouponId: String): List<CouponHistoryEntity>
}
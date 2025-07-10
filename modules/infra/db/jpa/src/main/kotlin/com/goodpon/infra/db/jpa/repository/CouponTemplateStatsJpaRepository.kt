package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.CouponTemplateStatsEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface CouponTemplateStatsJpaRepository : JpaRepository<CouponTemplateStatsEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CouponTemplateStatsEntity c WHERE c.couponTemplateId = :couponTemplateId")
    fun findByCouponTemplateIdForUpdate(couponTemplateId: Long): CouponTemplateStatsEntity?
}
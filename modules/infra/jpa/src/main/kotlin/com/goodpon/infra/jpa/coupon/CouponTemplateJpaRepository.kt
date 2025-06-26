package com.goodpon.infra.jpa.coupon

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface CouponTemplateJpaRepository : JpaRepository<CouponTemplateEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query("SELECT c FROM CouponTemplateEntity c WHERE c.id = :id")
    fun findByIdForRead(id: Long): CouponTemplateEntity?
}
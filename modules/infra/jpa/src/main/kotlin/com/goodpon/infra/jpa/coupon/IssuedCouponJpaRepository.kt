package com.goodpon.infra.jpa.coupon

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface IssuedCouponJpaRepository : JpaRepository<IssuedCouponEntity, String> {

    fun findFirstByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): IssuedCouponEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ic FROM IssuedCouponEntity ic WHERE ic.id = :id")
    fun findByIdForUpdate(id: String): IssuedCouponEntity?
}

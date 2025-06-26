package com.goodpon.infra.jpa.coupon

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface UserCouponJpaRepository : JpaRepository<UserCouponEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ic FROM UserCouponEntity ic WHERE ic.id = :id")
    fun findByIdForUpdate(id: String): UserCouponEntity?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean
}

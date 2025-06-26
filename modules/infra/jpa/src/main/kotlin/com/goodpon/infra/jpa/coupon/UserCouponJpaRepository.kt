package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.CouponStatus
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface UserCouponJpaRepository : JpaRepository<UserCouponEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ic FROM UserCouponEntity ic WHERE ic.id = :id")
    fun findByIdForUpdate(id: String): UserCouponEntity?

    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean

    fun findByStatusAndExpiresAtLessThanEqual(status: CouponStatus, expiresAt: LocalDateTime): List<UserCouponEntity>
}

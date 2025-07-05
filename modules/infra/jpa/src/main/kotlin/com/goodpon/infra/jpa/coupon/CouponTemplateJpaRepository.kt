package com.goodpon.infra.jpa.coupon

import com.goodpon.domain.domain.coupon.template.vo.CouponTemplateStatus
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface CouponTemplateJpaRepository : JpaRepository<CouponTemplateEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query("SELECT c FROM CouponTemplateEntity c WHERE c.id = :id")
    fun findByIdForRead(id: Long): CouponTemplateEntity?

    fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplateEntity>
}
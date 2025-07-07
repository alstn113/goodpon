package com.goodpon.infra.jpa.coupon.repository

import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.jpa.coupon.entity.CouponTemplateEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CouponTemplateJpaRepository : JpaRepository<CouponTemplateEntity, Long> {

    fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplateEntity>
}
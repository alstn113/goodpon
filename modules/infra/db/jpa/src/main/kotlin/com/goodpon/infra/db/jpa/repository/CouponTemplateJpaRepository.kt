package com.goodpon.infra.db.jpa.repository

import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.infra.db.jpa.entity.CouponTemplateEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CouponTemplateJpaRepository : JpaRepository<CouponTemplateEntity, Long> {

    fun findByStatusAndAbsoluteExpiresAtLessThanEqual(
        status: CouponTemplateStatus,
        absoluteExpiresAt: LocalDateTime,
    ): List<CouponTemplateEntity>
}
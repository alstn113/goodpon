package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.CouponTemplateStatsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponTemplateStatsJpaRepository : JpaRepository<CouponTemplateStatsEntity, Long>
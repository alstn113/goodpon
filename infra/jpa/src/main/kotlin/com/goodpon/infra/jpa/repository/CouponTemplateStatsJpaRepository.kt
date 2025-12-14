package com.goodpon.infra.jpa.repository

import com.goodpon.infra.jpa.entity.CouponTemplateStatsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponTemplateStatsJpaRepository : JpaRepository<CouponTemplateStatsEntity, Long>
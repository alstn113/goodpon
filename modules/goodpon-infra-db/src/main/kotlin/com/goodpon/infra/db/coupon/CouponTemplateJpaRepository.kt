package com.goodpon.infra.db.coupon

import org.springframework.data.jpa.repository.JpaRepository

interface CouponTemplateJpaRepository : JpaRepository<CouponTemplateEntity, Long>
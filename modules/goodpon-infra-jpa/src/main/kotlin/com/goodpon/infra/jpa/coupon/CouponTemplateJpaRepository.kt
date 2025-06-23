package com.goodpon.infra.jpa.coupon

import org.springframework.data.jpa.repository.JpaRepository

interface CouponTemplateJpaRepository : JpaRepository<CouponTemplateEntity, Long>
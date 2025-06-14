package com.goodpon.common.infra.persistence.coupon

import org.springframework.data.jpa.repository.JpaRepository

interface CouponTemplateJpaRepository : JpaRepository<CouponTemplateEntity, Long>
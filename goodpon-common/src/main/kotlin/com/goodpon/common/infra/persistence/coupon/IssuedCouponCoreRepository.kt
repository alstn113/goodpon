package com.goodpon.common.infra.persistence.coupon

import com.goodpon.common.domain.coupon.IssuedCouponRepository
import org.springframework.stereotype.Repository

@Repository
class IssuedCouponCoreRepository(
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) : IssuedCouponRepository
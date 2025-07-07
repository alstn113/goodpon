package com.goodpon.infra.jpa.coupon

import com.goodpon.infra.jpa.coupon.adapter.UserCouponJpaAdapter
import com.goodpon.infra.jpa.coupon.repository.UserCouponJpaRepository
import com.goodpon.infra.jpa.AbstractJpaIntegrationTest

class UserCouponJpaAdapterIT(
    private val userCouponJpaAdapter: UserCouponJpaAdapter,
    private val userCouponJpaRepository: UserCouponJpaRepository,
) : AbstractJpaIntegrationTest()
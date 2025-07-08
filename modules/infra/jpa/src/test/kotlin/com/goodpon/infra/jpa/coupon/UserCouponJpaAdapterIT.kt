package com.goodpon.infra.jpa.coupon

import com.goodpon.infra.jpa.AbstractIntegrationTest
import com.goodpon.infra.jpa.coupon.adapter.UserCouponJpaAdapter
import com.goodpon.infra.jpa.coupon.repository.UserCouponJpaRepository

class UserCouponJpaAdapterIT(
    private val userCouponJpaAdapter: UserCouponJpaAdapter,
    private val userCouponJpaRepository: UserCouponJpaRepository,
) : AbstractIntegrationTest()
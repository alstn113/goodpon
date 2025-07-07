package com.goodpon.infra.jpa.coupon

import com.goodpon.infra.jpa.coupon.adapter.UserCouponJpaAdapter
import com.goodpon.infra.jpa.coupon.repository.UserCouponJpaRepository
import com.goodpon.infra.jpa.IntegrationTest

class UserCouponJpaAdapterIT(
    private val userCouponJpaAdapter: UserCouponJpaAdapter,
    private val userCouponJpaRepository: UserCouponJpaRepository,
) : IntegrationTest()
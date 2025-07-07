package com.goodpon.infra.jpa.coupon

import com.goodpon.infra.jpa.account.repository.AccountJpaRepository
import com.goodpon.infra.jpa.coupon.adapter.CouponHistoryJpaAdapter
import com.goodpon.infra.jpa.IntegrationTest

class CouponHistoryJpaAdapterIT(
    private val couponHistoryJpaAdapter: CouponHistoryJpaAdapter,
    private val couponHistoryJpaRepository: AccountJpaRepository,
) : IntegrationTest()
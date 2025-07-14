package com.goodpon.partner.openapi.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.partner.application.coupon.port.`in`.CancelCouponRedemptionUseCase
import com.goodpon.partner.application.coupon.port.`in`.IssueCouponUseCase
import com.goodpon.partner.application.coupon.port.`in`.RedeemCouponUseCase
import com.goodpon.partner.openapi.controller.v1.CouponController
import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    value = [
        CouponController::class
    ]
)
abstract class AbstractWebTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockkBean
    protected lateinit var issueCouponUseCase: IssueCouponUseCase

    @MockkBean
    protected lateinit var redeemCouponUseCase: RedeemCouponUseCase

    @MockkBean
    protected lateinit var cancelCouponRedemptionUseCase: CancelCouponRedemptionUseCase
}

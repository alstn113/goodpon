package com.goodpon.api.partner.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.api.partner.controller.v1.CouponController
import com.goodpon.api.partner.controller.v1.CouponQueryController
import com.goodpon.api.partner.response.TraceIdProvider
import com.goodpon.application.partner.coupon.port.`in`.*
import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    value = [
        CouponController::class,
        CouponQueryController::class
    ],
)
abstract class AbstractWebTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockkBean
    protected lateinit var traceIdProvider: TraceIdProvider

    // Coupon Controller
    @MockkBean
    protected lateinit var issueCouponUseCase: IssueCouponUseCase

    @MockkBean
    protected lateinit var redeemCouponUseCase: RedeemCouponUseCase

    @MockkBean
    protected lateinit var cancelCouponRedemptionUseCase: CancelCouponRedemptionUseCase

    // Coupon Query Controller
    @MockkBean
    protected lateinit var getUserCouponsUseCase: GetUserCouponsUseCase

    @MockkBean
    protected lateinit var getAvailableUserCouponsUseCase: GetAvailableUserCouponsUseCase

    @MockkBean
    protected lateinit var getCouponTemplateDetailForUserUseCase: GetCouponTemplateDetailForUserUseCase
}

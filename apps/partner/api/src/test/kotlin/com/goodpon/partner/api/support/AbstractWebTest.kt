package com.goodpon.partner.api.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.partner.api.config.WebConfig
import com.goodpon.partner.api.controller.v1.CouponController
import com.goodpon.partner.api.controller.v1.CouponQueryController
import com.goodpon.partner.api.interceptor.IdempotencyInterceptor
import com.goodpon.partner.api.security.filter.TraceIdProvider
import com.goodpon.partner.application.coupon.port.`in`.*
import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    value = [
        CouponController::class,
        CouponQueryController::class
    ],
)
@Import(WebConfig::class)
abstract class AbstractWebTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockkBean
    protected lateinit var traceIdProvider: TraceIdProvider

    @MockkBean
    protected lateinit var idempotencyInterceptor: IdempotencyInterceptor

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

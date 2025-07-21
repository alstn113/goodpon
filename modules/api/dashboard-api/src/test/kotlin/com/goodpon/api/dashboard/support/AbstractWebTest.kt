package com.goodpon.api.dashboard.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.api.dashboard.controller.v1.account.AccountController
import com.goodpon.api.dashboard.controller.v1.auth.AuthController
import com.goodpon.api.dashboard.controller.v1.coupon.CouponTemplateController
import com.goodpon.api.dashboard.controller.v1.merchant.MerchantController
import com.goodpon.application.dashboard.account.port.`in`.GetAccountInfoUseCase
import com.goodpon.application.dashboard.account.port.`in`.SignUpUseCase
import com.goodpon.application.dashboard.auth.port.`in`.LoginUseCase
import com.goodpon.application.dashboard.auth.port.`in`.ResendVerificationEmailUseCase
import com.goodpon.application.dashboard.auth.port.`in`.VerifyEmailUseCase
import com.goodpon.application.dashboard.coupon.port.`in`.*
import com.goodpon.application.dashboard.merchant.port.`in`.CreateMerchantUseCase
import com.goodpon.application.dashboard.merchant.port.`in`.GetMyMerchantDetailUseCase
import com.goodpon.application.dashboard.merchant.port.`in`.GetMyMerchantsUseCase
import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    value = [
        AccountController::class,
        AuthController::class,
        CouponTemplateController::class,
        MerchantController::class
    ],
)
abstract class AbstractWebTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    // Account
    @MockkBean
    protected lateinit var signUpUseCase: SignUpUseCase

    @MockkBean
    protected lateinit var getAccountInfoUseCase: GetAccountInfoUseCase

    // Auth
    @MockkBean
    protected lateinit var loginUseCase: LoginUseCase

    @MockkBean
    protected lateinit var verifyEmailUseCase: VerifyEmailUseCase

    @MockkBean
    protected lateinit var resendVerificationEmailUseCase: ResendVerificationEmailUseCase

    // Coupon Template
    @MockkBean
    protected lateinit var createCouponTemplateUseCase: CreateCouponTemplateUseCase

    @MockkBean
    protected lateinit var publishCouponTemplateUseCase: PublishCouponTemplateUseCase

    @MockkBean
    protected lateinit var getMerchantCouponTemplateDetailUseCase: GetMerchantCouponTemplateDetailUseCase

    @MockkBean
    protected lateinit var getMerchantCouponTemplatesUseCase: GetMerchantCouponTemplatesUseCase

    @MockkBean
    protected lateinit var getCouponHistoriesUseCase: GetCouponHistoriesUseCase

    // Merchant
    @MockkBean
    protected lateinit var createMerchantUseCase: CreateMerchantUseCase

    @MockkBean
    protected lateinit var getMyMerchantsUseCase: GetMyMerchantsUseCase

    @MockkBean
    protected lateinit var getMyMerchantDetailUseCase: GetMyMerchantDetailUseCase
}

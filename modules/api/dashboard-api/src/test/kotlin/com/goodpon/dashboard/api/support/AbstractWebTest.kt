package com.goodpon.dashboard.api.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.dashboard.api.controller.v1.account.AccountController
import com.goodpon.dashboard.api.controller.v1.auth.AuthController
import com.goodpon.dashboard.api.controller.v1.coupon.CouponTemplateController
import com.goodpon.dashboard.api.controller.v1.merchant.MerchantController
import com.goodpon.dashboard.application.account.port.`in`.GetAccountInfoUseCase
import com.goodpon.dashboard.application.account.port.`in`.SignUpUseCase
import com.goodpon.dashboard.application.auth.port.`in`.LoginUseCase
import com.goodpon.dashboard.application.auth.port.`in`.ResendVerificationEmailUseCase
import com.goodpon.dashboard.application.auth.port.`in`.VerifyEmailUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.CreateCouponTemplateUseCase
import com.goodpon.dashboard.application.merchant.port.`in`.CreateMerchantUseCase
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

    @MockkBean
    protected lateinit var signUpUseCase: SignUpUseCase

    @MockkBean
    protected lateinit var getAccountInfoUseCase: GetAccountInfoUseCase

    @MockkBean
    protected lateinit var loginUseCase: LoginUseCase

    @MockkBean
    protected lateinit var verifyEmailUseCase: VerifyEmailUseCase

    @MockkBean
    protected lateinit var resendVerificationEmailUseCase: ResendVerificationEmailUseCase

    @MockkBean
    protected lateinit var createCouponTemplateUseCase: CreateCouponTemplateUseCase

    @MockkBean
    protected lateinit var merchantUseCase: CreateMerchantUseCase
}

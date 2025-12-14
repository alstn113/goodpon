package com.goodpon.dashboard.bootstrap.e2e

import com.goodpon.dashboard.api.controller.v1.auth.dto.LoginRequest
import com.goodpon.dashboard.api.controller.v1.coupon.dto.CreateCouponTemplateRequest
import com.goodpon.dashboard.api.response.ApiErrorDetail
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummaries
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.bootstrap.support.AbstractEndToEndTest
import com.goodpon.domain.account.Account
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.merchant.Merchant
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime


class CouponTemplateE2eTest(
    private val accountRepository: AccountRepository,
    private val merchantRepository: MerchantRepository,
    private val passwordEncoder: PasswordEncoder,
) : AbstractEndToEndTest() {

    @Test
    fun `쿠폰 템플릿 생성, 발행, 조회 시나리오`() {
        val (email, password, savedAccountId) = createAccount(verified = true)
        val merchantId = createMerchant(accountId = savedAccountId)

        val accessToken = `로그인 요청`(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .apply { id shouldBe savedAccountId }
            .accessToken

        `쿠폰 템플릿 목록 조회 요청`(accessToken = accessToken, merchantId = merchantId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateSummaries>()
            .apply { templates.size shouldBe 0 }

        val firstCouponTemplateId =
            `쿠폰 템플릿 생성 요청`(accessToken = accessToken, merchantId = merchantId, name = "여름 할인 쿠폰")
                .apply { statusCode() shouldBe 200 }
                .toApiResponse<CreateCouponTemplateResult>()
                .apply {
                    name shouldBe "여름 할인 쿠폰"
                    status shouldBe CouponTemplateStatus.DRAFT
                }.id

        `쿠폰 템플릿 목록 조회 요청`(accessToken = accessToken, merchantId = merchantId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateSummaries>()
            .apply {
                templates.size shouldBe 1
                templates.first().name shouldBe "여름 할인 쿠폰"
            }

        val secondCouponTemplateId =
            `쿠폰 템플릿 생성 요청`(accessToken = accessToken, merchantId = merchantId, name = "여행 할인 쿠폰")
                .apply { statusCode() shouldBe 200 }
                .toApiResponse<CreateCouponTemplateResult>()
                .apply {
                    name shouldBe "여행 할인 쿠폰"
                    status shouldBe CouponTemplateStatus.DRAFT
                }.id

        `쿠폰 템플릿 발행 요청`(accessToken = accessToken, merchantId = merchantId, couponTemplateId = firstCouponTemplateId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<PublishCouponTemplateResult>()
            .apply {
                id shouldBe firstCouponTemplateId
                status shouldBe CouponTemplateStatus.ISSUABLE
            }

        `쿠폰 템플릿 목록 조회 요청`(accessToken = accessToken, merchantId = merchantId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateSummaries>()
            .apply {
                templates.size shouldBe 2
                templates.first().id shouldBe secondCouponTemplateId
                templates.first().name shouldBe "여행 할인 쿠폰"
                templates.first().status shouldBe CouponTemplateStatus.DRAFT
                templates.last().id shouldBe firstCouponTemplateId
                templates.last().name shouldBe "여름 할인 쿠폰"
                templates.last().status shouldBe CouponTemplateStatus.ISSUABLE
            }

        `쿠폰 템플릿 상세 조회 요청`(accessToken = accessToken, merchantId = merchantId, couponTemplateId = firstCouponTemplateId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateSummary>()
            .apply {
                id shouldBe firstCouponTemplateId
                name shouldBe "여름 할인 쿠폰"
                status shouldBe CouponTemplateStatus.ISSUABLE
            }
    }

    @Test
    fun `유효하지 않은 데이터로 쿠폰 템플릿 생성 시나리오`() {
        val (email, password, savedAccountId) = createAccount(verified = true)
        val merchantId = createMerchant(accountId = savedAccountId)

        val accessToken = `로그인 요청`(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .apply { id shouldBe savedAccountId }
            .accessToken

        val request = CreateCouponTemplateRequest(
            name = "테스트 쿠폰",
            description = "쿠폰 템플릿 설명",
            minOrderAmount = 0, // 최소 주문 금액이 있을 경우 0보다 커야 함
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 1000,
            maxDiscountAmount = 5000, // 고정 금액 할인은 최대 할인 금액을 설정할 수 없음
            issueStartDate = LocalDate.now().plusDays(5),
            issueEndDate = LocalDate.now().plusDays(10),
            validityDays = 3,
            absoluteExpiryDate = LocalDate.now().minusDays(9), // 절대 만료일은 발행 종료일보다 이전일 수 없음
            limitType = CouponLimitPolicyType.REDEEM_COUNT,
            maxIssueCount = 100L, // 사용 제한 정책이 설정된 쿠폰은 발급 제한 수량을 함께 설정할 수 없음
            maxRedeemCount = 200L
        )

        val errorDetails = `쿠폰 템플릿 생성 요청`(
            accessToken = accessToken,
            merchantId = merchantId,
            name = "테스트 쿠폰",
            request = request
        ).apply { statusCode() shouldBe 400 }
            .toApiErrorResponse<Unit>()
            .extractErrorData<List<ApiErrorDetail>>()

        val expectedErrorDetails = listOf(
            ApiErrorDetail(field = "minOrderAmount", message = "쿠폰 사용 조건의 최소 주문 금액은 0보다 커야 합니다."),
            ApiErrorDetail(field = "maxDiscountAmount", message = "고정 금액 할인은 최대 할인 금액을 설정할 수 없습니다."),
            ApiErrorDetail(field = "absoluteExpiryDate", message = "쿠폰 사용 절대 만료일은 발행 시작일보다 이전일 수 없습니다."),
            ApiErrorDetail(field = "maxIssueCount", message = "사용 제한 정책이 설정된 쿠폰은 발급 제한 수량을 함께 설정할 수 없습니다.")
        )

        errorDetails shouldBe expectedErrorDetails
    }

    @Test
    fun `인증되지 않은 사용자 시나리오`() {
        val (email, password, savedAccountId) = createAccount(verified = false)
        val merchantId = createMerchant(accountId = savedAccountId)

        val accessToken = `로그인 요청`(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .apply { id shouldBe savedAccountId }
            .accessToken

        `쿠폰 템플릿 목록 조회 요청`(accessToken = accessToken, merchantId = merchantId)
            .apply { statusCode() shouldBe 403 }
            .toApiErrorResponse<Unit>()
            .apply { message shouldBe "계정이 아직 인증되지 않았습니다." }

        `쿠폰 템플릿 상세 조회 요청`(accessToken = accessToken, merchantId = merchantId, couponTemplateId = 1L)
            .apply { statusCode() shouldBe 403 }
            .toApiErrorResponse<Unit>()
            .apply { message shouldBe "계정이 아직 인증되지 않았습니다." }

        `쿠폰 템플릿 생성 요청`(accessToken = accessToken, merchantId = merchantId, name = "테스트 쿠폰")
            .apply { statusCode() shouldBe 403 }
            .toApiErrorResponse<Unit>()
            .apply { message shouldBe "계정이 아직 인증되지 않았습니다." }

        `쿠폰 템플릿 발행 요청`(accessToken = accessToken, merchantId = merchantId, couponTemplateId = 1L)
            .apply { statusCode() shouldBe 403 }
            .toApiErrorResponse<Unit>()
            .apply { message shouldBe "계정이 아직 인증되지 않았습니다." }
    }

    private fun `로그인 요청`(email: String, password: String): Response {
        val request = LoginRequest(email = email, password = password)

        return given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/login")
    }

    private fun `쿠폰 템플릿 목록 조회 요청`(accessToken: String, merchantId: Long): Response {
        return given()
            .header("Authorization", "Bearer $accessToken")
            .`when`()
            .get("/v1/merchants/$merchantId/coupon-templates")
    }

    private fun `쿠폰 템플릿 상세 조회 요청`(accessToken: String, merchantId: Long, couponTemplateId: Long): Response {
        return given()
            .header("Authorization", "Bearer $accessToken")
            .`when`()
            .get("/v1/merchants/$merchantId/coupon-templates/$couponTemplateId")
    }

    private fun `쿠폰 템플릿 생성 요청`(
        accessToken: String,
        merchantId: Long,
        name: String,
        request: CreateCouponTemplateRequest = defaultCreateCouponTemplateRequest(name),
    ): Response {
        return given()
            .header("Authorization", "Bearer $accessToken")
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/merchants/$merchantId/coupon-templates")
    }

    private fun `쿠폰 템플릿 발행 요청`(
        accessToken: String,
        merchantId: Long,
        couponTemplateId: Long,
    ): Response {
        return given()
            .header("Authorization", "Bearer $accessToken")
            .`when`()
            .post("/v1/merchants/$merchantId/coupon-templates/$couponTemplateId/publish")
    }

    private fun createAccount(verified: Boolean): Triple<String, String, Long> {
        val email = "test@goodpon.site"
        val password = "password"

        val account = Account.create(
            email = email,
            password = passwordEncoder.encode(password),
            name = "테스트 상점"
        ).let {
            if (verified) {
                it.verify(LocalDateTime.now())
            } else {
                it
            }
        }

        val savedAccount = accountRepository.save(account)
        return Triple(email, password, savedAccount.id)
    }

    private fun createMerchant(accountId: Long): Long {
        val merchant = Merchant.create(
            name = "테스트 상점",
            accountId = accountId
        )
        val savedMerchant = merchantRepository.save(merchant)

        return savedMerchant.id
    }

    private fun defaultCreateCouponTemplateRequest(name: String): CreateCouponTemplateRequest {
        return CreateCouponTemplateRequest(
            name = name,
            description = "쿠폰 템플릿 설명",
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 1000,
            maxDiscountAmount = null,
            issueStartDate = LocalDate.now(),
            issueEndDate = LocalDate.now().plusDays(30),
            validityDays = 30,
            absoluteExpiryDate = LocalDate.now().plusDays(40),
            limitType = CouponLimitPolicyType.ISSUE_COUNT,
            maxIssueCount = 100L,
            maxRedeemCount = null
        )
    }
}
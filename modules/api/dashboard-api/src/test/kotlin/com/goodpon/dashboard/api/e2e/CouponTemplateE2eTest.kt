package com.goodpon.dashboard.api.e2e

import com.goodpon.dashboard.api.controller.v1.auth.dto.LoginRequest
import com.goodpon.dashboard.api.controller.v1.coupon.dto.CreateCouponTemplateRequest
import com.goodpon.dashboard.api.support.AbstractEndToEndTest
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.dashboard.application.coupon.port.`in`.dto.PublishCouponTemplateResult
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
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
        val merchantId = createMerchant(savedAccountId)

        val accessToken = `로그인 요청`(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .apply { id shouldBe savedAccountId }
            .accessToken

        `쿠폰 템플릿 목록 조회 요청`(accessToken = accessToken, merchantId = merchantId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<List<CouponTemplateSummary>>()
            .apply { size shouldBe 0 }

        val firstCouponTemplateId =
            `쿠폰 템플릿 생성 요청`(accessToken = accessToken, merchantId = merchantId, name = "여름 할인 쿠폰")
                .apply { statusCode() shouldBe 200 }
                .toApiResponse<CouponTemplateSummary>()
                .apply {
                    name shouldBe "물놀이 할인 쿠폰"
                    status shouldBe CouponTemplateStatus.DRAFT
                }.id

        `쿠폰 템플릿 목록 조회 요청`(accessToken = accessToken, merchantId = merchantId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<List<CouponTemplateSummary>>()
            .apply {
                size shouldBe 1
                first().name shouldBe "여름 할인 쿠폰"
            }

        val secondCouponTemplateId =
            `쿠폰 템플릿 생성 요청`(accessToken = accessToken, merchantId = merchantId, name = "여행 할인 쿠폰")
                .apply { statusCode() shouldBe 200 }
                .toApiResponse<CouponTemplateSummary>()
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
            .toApiResponse<List<CouponTemplateSummary>>()
            .apply {
                size shouldBe 2
                first().id shouldBe secondCouponTemplateId
                first().name shouldBe "여행 할인 쿠폰"
                first().status shouldBe CouponTemplateStatus.ISSUABLE
                last().id shouldBe firstCouponTemplateId
                last().name shouldBe "여름 할인 쿠폰"
                last().status shouldBe CouponTemplateStatus.DRAFT
            }

        `쿠폰 템플릿 상세 조회 요청`(accessToken = accessToken, merchantId = merchantId, couponTemplateId = firstCouponTemplateId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateSummary>()
            .apply {
                id shouldBe secondCouponTemplateId
                name shouldBe "여행 할인 쿠폰"
                status shouldBe CouponTemplateStatus.ISSUABLE
            }
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
    ): Response {
        val request = CreateCouponTemplateRequest(
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
        ).apply {
            if (verified) {
                this.verify(LocalDateTime.now())
            }
        }
        val savedAccount = accountRepository.save(account)

        return Triple(
            email,
            password,
            savedAccount.id
        )
    }

    private fun createMerchant(accountId: Long): Long {
        val merchant = Merchant.create(
            name = "테스트 상점",
            accountId = accountId
        )
        val savedMerchant = merchantRepository.save(merchant)

        return savedMerchant.id
    }
}
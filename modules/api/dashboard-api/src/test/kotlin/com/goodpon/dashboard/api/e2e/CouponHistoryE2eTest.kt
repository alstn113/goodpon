package com.goodpon.dashboard.api.e2e

import com.goodpon.dashboard.api.controller.v1.auth.dto.LoginRequest
import com.goodpon.dashboard.api.controller.v1.coupon.dto.CouponHistorySearchRequest
import com.goodpon.dashboard.api.response.ApiErrorDetail
import com.goodpon.dashboard.api.support.AbstractEndToEndTest
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.domain.merchant.MerchantClientSecret
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class CouponHistoryE2eTest(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val merchantRepository: MerchantRepository,
) : AbstractEndToEndTest() {

    @Test
    fun `쿠폰 내역 조회 유효성 검사 실패`() {
        val email = "test@goodpon.site"
        val password = "test-password"
        val account = createAccount(email, password)
        val merchant = createMerchant(account.id)

        val accessToken = `로그인 요청`(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .accessToken

        val couponHistorySearchRequest = CouponHistorySearchRequest(
            startDate = LocalDate.now(),
            endDate = LocalDate.now().minusDays(1),
            size = 200,
        )

        val response = `쿠폰 내역 조회`(
            merchantId = merchant.id,
            accessToken = accessToken,
            request = couponHistorySearchRequest
        ).apply { statusCode() shouldBe 400 }
            .toApiErrorResponse<Unit>()

        response.code shouldBe "BAD_REQUEST"

        val errorDetails = response.extractErrorData<List<ApiErrorDetail>>()
        val expectedErrorDetails = listOf(
            ApiErrorDetail(
                field = "size",
                message = "size는 1 이상 100 이하이어야 합니다."
            ),
            ApiErrorDetail(
                field = "startDate",
                message = "startDate는 endDate보다 이후일 수 없습니다."
            ),
        )

        errorDetails shouldContainExactlyInAnyOrder expectedErrorDetails
    }

    private fun `쿠폰 내역 조회`(
        merchantId: Long,
        accessToken: String,
        request: CouponHistorySearchRequest,
    ): Response {
        return given()
            .withAuthHeader(accessToken)
            .queryParam("startDate", request.startDate?.toString())
            .queryParam("endDate", request.endDate?.toString())
            .queryParam("couponTemplateId", request.couponTemplateId)
            .queryParam("userId", request.userId)
            .queryParam("orderId", request.orderId)
            .queryParam("nextCursor", request.nextCursor)
            .queryParam("size", request.size)
            .`when`()
            .get("/v1/merchants/${merchantId}/coupon-histories")
    }

    private fun `로그인 요청`(email: String, password: String): Response {
        val request = LoginRequest(email = email, password = password)

        return given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/login")
    }

    private fun createAccount(email: String, password: String): Account {
        return accountRepository.save(
            Account.create(
                email = email,
                password = passwordEncoder.encode(password),
                name = "테스트 계정"
            ).verify(LocalDateTime.now())
        )
    }

    private fun createMerchant(accountId: Long): Merchant {
        return merchantRepository.save(
            Merchant(
                name = "테스트 상점",
                clientId = "ck_client_id",
                secrets = listOf(MerchantClientSecret.create("sk_client_secret")),
                accounts = listOf(MerchantAccount.createOwner(accountId = accountId))
            )
        )
    }
}
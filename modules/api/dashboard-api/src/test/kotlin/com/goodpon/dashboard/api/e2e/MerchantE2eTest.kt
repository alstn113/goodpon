package com.goodpon.dashboard.api.e2e

import com.goodpon.dashboard.api.controller.v1.auth.dto.LoginRequest
import com.goodpon.dashboard.api.controller.v1.merchant.dto.CreateMerchantRequest
import com.goodpon.dashboard.api.support.AbstractEndToEndTest
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.auth.port.`in`.dto.LoginResult
import com.goodpon.dashboard.application.auth.port.out.PasswordEncoder
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantResult
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantDetail
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummaries
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummary
import com.goodpon.domain.account.Account
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MerchantE2eTest(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) : AbstractEndToEndTest() {

    @Test
    fun `상점 생성 시나리오`() {
        val (email, password, savedAccountId) = createAccount(verified = true)

        val (accountId, accessToken) = 로그인_요청(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .apply { id shouldBe savedAccountId }
            .let { it.id to it.accessToken }

        내_상점_목록_조회_요청(accessToken = accessToken)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<MyMerchantSummaries>()
            .apply { merchants.size shouldBe 0 }

        val newMerchantName = "테스트 상점"
        val merchantId = 상점_생성_요청(accessToken = accessToken, name = newMerchantName)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<CreateMerchantResult>()
            .id

        내_상점_목록_조회_요청(accessToken = accessToken)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<MyMerchantSummaries>()
            .apply {
                merchants.size shouldBe 1
                merchants.first().name shouldBe newMerchantName
            }

        상점_상세_조회_요청(accessToken = accessToken, merchantId = merchantId)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<MyMerchantDetail>()
            .apply {
                id shouldBe merchantId
                name shouldBe newMerchantName
                merchantAccounts.first().accountId shouldBe accountId
                clientSecrets.size shouldBe 1
            }
    }

    @Test
    fun `인증되지 않은 사용자 시나리오`() {
        val (email, password, savedAccountId) = createAccount(verified = false)

        val accessToken = 로그인_요청(email = email, password = password)
            .apply { statusCode() shouldBe 200 }
            .toApiResponse<LoginResult>()
            .apply { id shouldBe savedAccountId }
            .accessToken

        내_상점_목록_조회_요청(accessToken = accessToken)
            .apply { statusCode() shouldBe 403 }
            .toApiErrorResponse<Unit>()
            .apply { message shouldBe "계정이 아직 인증되지 않았습니다." }

        상점_상세_조회_요청(accessToken = accessToken, merchantId = 1L)
            .apply { statusCode() shouldBe 403 }
            .toApiErrorResponse<Unit>()
            .apply { message shouldBe "계정이 아직 인증되지 않았습니다." }

        상점_생성_요청(accessToken = accessToken, name = "테스트 상점")
            .apply { statusCode() shouldBe 403 }
            .toApiErrorResponse<Unit>()
            .apply { message shouldBe "계정이 아직 인증되지 않았습니다." }
    }

    private fun 로그인_요청(email: String, password: String): Response {
        val request = LoginRequest(email = email, password = password)

        return given()
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/auth/login")
    }

    private fun 내_상점_목록_조회_요청(accessToken: String): Response {
        return given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer $accessToken")
            .`when`()
            .get("/v1/merchants")
    }

    private fun 상점_생성_요청(accessToken: String, name: String): Response {
        val request = CreateMerchantRequest(name = name)

        return given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer $accessToken")
            .body(request)
            .`when`()
            .post("/v1/merchants")
    }

    private fun 상점_상세_조회_요청(accessToken: String, merchantId: Long): Response {
        return given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer $accessToken")
            .`when`()
            .get("/v1/merchants/$merchantId")
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
}

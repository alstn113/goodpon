package com.goodpon.partner.bootstrap.e2e

import com.goodpon.partner.api.security.ApiKeyHeader
import com.goodpon.partner.bootstrap.support.AbstractEndToEndTest
import com.goodpon.partner.bootstrap.support.accessor.TestMerchantAccessor
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured.given
import io.restassured.response.Response
import org.junit.jupiter.api.Test

class AuthE2eTest(
    private val testMerchantAccessor: TestMerchantAccessor,
) : AbstractEndToEndTest() {

    @Test
    fun `Client Id가 존재하지 않는 경우`() {
        `사용자가 보유한 쿠폰 목록 조회 요청`(
            clientId = null,
            clientSecret = "sk_invalid_client_secret",
            userId = "unique_user_id"
        ).apply { statusCode() shouldBe 400 }
            .toApiErrorResponse<Unit>()
            .apply {
                code shouldBe "CLIENT_ID_MISSING"
                message shouldBe "Client Id가 누락되었습니다."
            }
    }

    @Test
    fun `Client Secret이 존재하지 않는 경우`() {
        `사용자가 보유한 쿠폰 목록 조회 요청`(
            clientId = "ck_invalid_client_secret",
            clientSecret = null,
            userId = "unique_user_id"
        ).apply { statusCode() shouldBe 400 }
            .toApiErrorResponse<Unit>()
            .apply {
                code shouldBe "CLIENT_SECRET_MISSING"
                message shouldBe "Client Secret이 누락되었습니다."
            }
    }

    @Test
    fun `상점이 존재하지 않는 경우 - Client Id가 유효하지 않은 경우`() {
        val (_, _, clientSecret) = testMerchantAccessor.createMerchant()

        `사용자가 보유한 쿠폰 목록 조회 요청`(
            clientId = "ck_invalid_client_id",
            clientSecret = clientSecret,
            userId = "unique_user_id"
        ).apply { statusCode() shouldBe 401 }
            .toApiErrorResponse<Unit>()
            .apply {
                code shouldBe "INVALID_CREDENTIALS"
                message shouldBe "인증 정보가 유효하지 않습니다. Client ID와 Client Secret을 확인해주세요."
            }
    }

    @Test
    fun `Client Id와 Client Secret이 맞지 않는 경우`() {
        val (_, clientId, _) = testMerchantAccessor.createMerchant()

        `사용자가 보유한 쿠폰 목록 조회 요청`(
            clientId = clientId,
            clientSecret = "sk_invalid_client_secret",
            userId = "unique_user_id"
        ).apply { statusCode() shouldBe 401 }
            .toApiErrorResponse<Unit>()
            .apply {
                code shouldBe "INVALID_CREDENTIALS"
                message shouldBe "인증 정보가 유효하지 않습니다. Client ID와 Client Secret을 확인해주세요."
            }
    }

    private fun `사용자가 보유한 쿠폰 목록 조회 요청`(
        clientId: String?,
        clientSecret: String?,
        userId: String,
    ): Response {
        return given()
            .apply {
                if (clientId != null) {
                    header(ApiKeyHeader.CLIENT_ID.headerName, clientId)
                }
                if (clientSecret != null) {
                    header(ApiKeyHeader.CLIENT_SECRET.headerName, clientSecret)
                }
            }
            .queryParam("userId", userId)
            .`when`()
            .get("/v1/user-coupons")
    }
}
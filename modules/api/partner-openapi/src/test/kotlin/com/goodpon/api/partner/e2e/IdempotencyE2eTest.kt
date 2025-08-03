package com.goodpon.api.partner.e2e

import com.goodpon.api.partner.controller.v1.request.IssueCouponRequest
import com.goodpon.api.partner.support.AbstractEndToEndTest
import com.goodpon.api.partner.support.accessor.TestCouponTemplateAccessor
import com.goodpon.api.partner.support.accessor.TestMerchantAccessor
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponResult
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.junit.jupiter.api.Test

class IdempotencyE2eTest(
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
) : AbstractEndToEndTest() {

    @Test
    fun `멱등 키가 없어도 요청할 수 있다`() {
        val (merchantId, clientId, clientSecret) = testMerchantAccessor.createMerchant()
        val couponTemplateId = createCouponTemplate(merchantId)
        val userId = "unique-user-id"

        `쿠폰 발급 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = userId,
            couponTemplateId = couponTemplateId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<IssueCouponResult>()
            .apply { this.couponTemplateId shouldBe couponTemplateId }
    }

    @Test
    fun `동일한 멱등 키를 사용해 요청하는 경우 동일한 응답을 받을 수 있다`() {
        val (merchantId, clientId, clientSecret) = testMerchantAccessor.createMerchant()
        val couponTemplateId = createCouponTemplate(merchantId)
        val userId = "unique-user-id"
        val idempotencyKey = "unique-idempotency-key"

        // 첫 번째 요청
        `쿠폰 발급 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            idempotencyKey = idempotencyKey,
            userId = userId,
            couponTemplateId = couponTemplateId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<IssueCouponResult>()
            .apply { this.couponTemplateId shouldBe couponTemplateId }

        // 두 번째 요청 - 동일한 멱등 키 사용
        `쿠폰 발급 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            idempotencyKey = idempotencyKey,
            userId = userId,
            couponTemplateId = couponTemplateId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<IssueCouponResult>()
            .apply { this.couponTemplateId shouldBe couponTemplateId }
    }

    private fun `쿠폰 발급 요청`(
        clientId: String,
        clientSecret: String,
        idempotencyKey: String? = null,
        userId: String,
        couponTemplateId: Long,
    ): Response {
        val request = IssueCouponRequest(userId = userId)

        return given()
            .withApiKeyHeaders(clientId = clientId, clientSecret = clientSecret)
            .apply { idempotencyKey?.let { withIdempotencyKey(it) } }
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/coupon-templates/${couponTemplateId}/issue")
    }

    private fun createCouponTemplate(merchantId: Long): Long {
        return testCouponTemplateAccessor.createCouponTemplate(
            name = "여름 할인 쿠폰",
            merchantId = merchantId,
            minOrderAmount = 15000,
            discountType = CouponDiscountType.PERCENTAGE,
            discountValue = 10,
            maxDiscountAmount = 5000,
            maxIssueCount = 1L,
            maxRedeemCount = null,
            limitType = CouponLimitPolicyType.ISSUE_COUNT
        )
    }
}
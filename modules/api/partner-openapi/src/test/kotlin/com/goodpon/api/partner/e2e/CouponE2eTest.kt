package com.goodpon.api.partner.e2e

import com.goodpon.api.partner.controller.v1.request.CancelCouponRedemptionRequest
import com.goodpon.api.partner.controller.v1.request.IssueCouponRequest
import com.goodpon.api.partner.controller.v1.request.RedeemCouponRequest
import com.goodpon.api.partner.support.AbstractEndToEndTest
import com.goodpon.api.partner.support.accessor.TestCouponTemplateAccessor
import com.goodpon.api.partner.support.accessor.TestMerchantAccessor
import com.goodpon.api.partner.support.accessor.TestUserCouponAccessor
import com.goodpon.application.partner.coupon.port.`in`.dto.CancelCouponRedemptionResult
import com.goodpon.application.partner.coupon.port.`in`.dto.CouponIssuanceStatus
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponResult
import com.goodpon.application.partner.coupon.service.dto.AvailableUserCouponsView
import com.goodpon.application.partner.coupon.service.dto.CouponTemplateDetailForUser
import com.goodpon.application.partner.coupon.service.dto.UserCouponList
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.user.UserCouponStatus
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.junit.jupiter.api.Test
import java.util.*

class CouponE2eTest(
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
) : AbstractEndToEndTest() {

    @Test
    fun `쿠폰 발급, 사용, 사용 취소, 조회 시나리오`() {
        val (merchantId, clientId, clientSecret) = testMerchantAccessor.createMerchant()
        val summerCouponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
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
        val winterCouponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            name = "겨울 할인 쿠폰",
            merchantId = merchantId,
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 2000,
            maxIssueCount = null,
            maxRedeemCount = 1L,
            limitType = CouponLimitPolicyType.REDEEM_COUNT
        )

        val emmaUserId = "goodpon-store-emma-unique-user-id"
        val stellaUserId = "goodpon-store-stella-unique-user-id"

        // 비로그인 회원의 여름 할인 쿠폰 템플릿 조회 (선착순 쿠폰 발급 페이지)
        `쿠폰 템플릿 상세 정보와 사용자에 따른 발급 가능 여부 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            couponTemplateId = summerCouponTemplateId,
            userId = null
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateDetailForUser>()
            .apply {
                name shouldBe "여름 할인 쿠폰"
                issuanceStatus shouldBe CouponIssuanceStatus.AVAILABLE
                currentIssueCount shouldBe 0
                currentRedeemCount shouldBe 0
            }

        // Emma의 여름 할인 쿠폰 템플릿 조회 (선착순 쿠폰 발급 페이지)
        `쿠폰 템플릿 상세 정보와 사용자에 따른 발급 가능 여부 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            couponTemplateId = summerCouponTemplateId,
            userId = emmaUserId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateDetailForUser>()
            .apply {
                name shouldBe "여름 할인 쿠폰"
                issuanceStatus shouldBe CouponIssuanceStatus.AVAILABLE
                currentIssueCount shouldBe 0
                currentRedeemCount shouldBe 0
            }

        // Emma의 여름 쿠폰 발급 요청
        `쿠폰 발급 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = emmaUserId,
            couponTemplateId = summerCouponTemplateId
        ).apply { statusCode() shouldBe 200 }
        val issuedEmmaSummerCouponId = testUserCouponAccessor.issueCouponAndRecord(
            userId = emmaUserId,
            couponTemplateId = summerCouponTemplateId,
            merchantId = merchantId
        ).id

        // Emma의 여름 할인 쿠폰 템플릿 조회 (선착순 쿠폰 발급 페이지)
        `쿠폰 템플릿 상세 정보와 사용자에 따른 발급 가능 여부 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            couponTemplateId = summerCouponTemplateId,
            userId = emmaUserId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateDetailForUser>()
            .apply {
                name shouldBe "여름 할인 쿠폰"
                issuanceStatus shouldBe CouponIssuanceStatus.ALREADY_ISSUED_BY_USER
                currentIssueCount shouldBe 1
                currentRedeemCount shouldBe 0
            }

        // 비로그인 회원의 여름 할인 쿠폰 템플릿 조회 (선착순 쿠폰 발급 페이지)
        `쿠폰 템플릿 상세 정보와 사용자에 따른 발급 가능 여부 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            couponTemplateId = summerCouponTemplateId,
            userId = null
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateDetailForUser>()
            .apply {
                name shouldBe "여름 할인 쿠폰"
                issuanceStatus shouldBe CouponIssuanceStatus.MAX_ISSUE_COUNT_EXCEEDED
                currentIssueCount shouldBe 1
                currentRedeemCount shouldBe 0
            }

        // Emma의 겨울 할인 쿠폰 템플릿 조회 (선착순 쿠폰 발급 페이지)
        `쿠폰 템플릿 상세 정보와 사용자에 따른 발급 가능 여부 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            couponTemplateId = winterCouponTemplateId,
            userId = emmaUserId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateDetailForUser>()
            .apply {
                name shouldBe "겨울 할인 쿠폰"
                issuanceStatus shouldBe CouponIssuanceStatus.AVAILABLE
                currentIssueCount shouldBe 0
                currentRedeemCount shouldBe 0
            }

        // Emma의 겨울 할인 쿠폰 발급 요청
        `쿠폰 발급 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = emmaUserId,
            couponTemplateId = winterCouponTemplateId
        ).apply { statusCode() shouldBe 200 }
        val issuedEmmaWinterCouponId = testUserCouponAccessor.issueCouponAndRecord(
            userId = emmaUserId,
            couponTemplateId = winterCouponTemplateId,
            merchantId = merchantId
        ).id

        // Stella의 겨울 할인 쿠폰 템플릿 조회 (선착순 쿠폰 발급 페이지)
        `쿠폰 템플릿 상세 정보와 사용자에 따른 발급 가능 여부 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            couponTemplateId = winterCouponTemplateId,
            userId = stellaUserId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateDetailForUser>()
            .apply {
                name shouldBe "겨울 할인 쿠폰"
                issuanceStatus shouldBe CouponIssuanceStatus.AVAILABLE
                currentIssueCount shouldBe 1
                currentRedeemCount shouldBe 0
            }

        // Stella의 겨울 할인 쿠폰 발급 요청
        `쿠폰 발급 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = stellaUserId,
            couponTemplateId = winterCouponTemplateId
        ).apply { statusCode() shouldBe 200 }
        val issuedStellaWinterCouponId = testUserCouponAccessor.issueCouponAndRecord(
            userId = stellaUserId,
            couponTemplateId = winterCouponTemplateId,
            merchantId = merchantId
        ).id

        // Emma의 보유한 쿠폰 목록 조회 (마이페이지)
        `사용자가 보유한 쿠폰 목록 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = emmaUserId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<UserCouponList>()
            .apply {
                val coupons = this.coupons
                coupons.size shouldBe 2
                // 나중에 발급 받은 쿠폰이 위쪽에 노출됨
                coupons[0].userCouponId shouldBe issuedEmmaWinterCouponId
                coupons[0].isRedeemable shouldBe true
                coupons[1].userCouponId shouldBe issuedEmmaSummerCouponId
                coupons[1].isRedeemable shouldBe true
            }

        // Emma의 사용 가능한 쿠폰 목록 조회 (주문 페이지)
        val emmaAvailableWinterCouponId = `사용자의 사용 가능한 쿠폰 목록 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = emmaUserId,
            orderAmount = 12000
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<AvailableUserCouponsView>()
            .apply {
                val coupons = this.coupons
                coupons.size shouldBe 2
                coupons[0].userCouponId shouldBe issuedEmmaWinterCouponId
                coupons[0].isMinOrderAmountReached shouldBe true
                coupons[1].userCouponId shouldBe issuedEmmaSummerCouponId
                coupons[1].isMinOrderAmountReached shouldBe false
            }
            .coupons[0].userCouponId

        // Emma의 겨울 할인 쿠폰 사용 (결제 페이지)
        val emmaOrderId = UUID.randomUUID().toString()
        `쿠폰 사용 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userCouponId = emmaAvailableWinterCouponId,
            userId = emmaUserId,
            orderAmount = 12000,
            orderId = emmaOrderId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<RedeemCouponResult>()
            .apply {
                userCouponId shouldBe emmaAvailableWinterCouponId
                orderId shouldBe emmaOrderId
                originalPrice shouldBe 12000
                discountAmount shouldBe 2000
                finalPrice shouldBe 10000
            }

        // Emma의 겨울 할인 쿠폰 사용 취소 (결제 실패)
        `쿠폰 사용 취소 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userCouponId = emmaAvailableWinterCouponId,
            orderId = emmaOrderId,
            cancelReason = "결제 실패"
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<CancelCouponRedemptionResult>()
            .apply {
                userCouponId shouldBe emmaAvailableWinterCouponId
                status shouldBe UserCouponStatus.ISSUED
                cancelReason shouldBe "결제 실패"
            }

        // Emma의 겨울 할인 쿠폰 사용 (결제 페이지)
        val emmaNewOrderId = UUID.randomUUID().toString()
        `쿠폰 사용 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userCouponId = emmaAvailableWinterCouponId,
            userId = emmaUserId,
            orderAmount = 12000,
            orderId = emmaNewOrderId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<RedeemCouponResult>()
            .apply {
                userCouponId shouldBe emmaAvailableWinterCouponId
                orderId shouldBe emmaNewOrderId
                originalPrice shouldBe 12000
                discountAmount shouldBe 2000
                finalPrice shouldBe 10000
            }

        // Emma의 보유한 쿠폰 목록 조회 (마이페이지)
        `사용자가 보유한 쿠폰 목록 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = emmaUserId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<UserCouponList>()
            .apply {
                val coupons = this.coupons
                coupons.size shouldBe 1
                coupons[0].userCouponId shouldBe issuedEmmaSummerCouponId
                coupons[0].isRedeemable shouldBe true
            }

        // Stella의 보유한 쿠폰 목록 조회 (마이페이지)
        `사용자가 보유한 쿠폰 목록 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = stellaUserId
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<UserCouponList>()
            .apply {
                val coupons = this.coupons
                coupons.size shouldBe 1
                coupons[0].userCouponId shouldBe issuedStellaWinterCouponId
                coupons[0].isRedeemable shouldBe false
            }

        // Stella의 사용 가능한 쿠폰 목록 조회 (주문 페이지)
        `사용자의 사용 가능한 쿠폰 목록 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            userId = stellaUserId,
            orderAmount = 16000
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<AvailableUserCouponsView>()
            .apply {
                val coupons = this.coupons
                coupons.size shouldBe 0
            }

        // 비로그인 회원의 겨울 할인 쿠폰 템플릿 조회 (선착순 쿠폰 발급 페이지)
        `쿠폰 템플릿 상세 정보와 사용자에 따른 발급 가능 여부 조회 요청`(
            clientId = clientId,
            clientSecret = clientSecret,
            couponTemplateId = winterCouponTemplateId,
            userId = null
        ).apply { statusCode() shouldBe 200 }
            .toApiResponse<CouponTemplateDetailForUser>()
            .apply {
                name shouldBe "겨울 할인 쿠폰"
                issuanceStatus shouldBe CouponIssuanceStatus.MAX_REDEEM_COUNT_EXCEEDED
                currentIssueCount shouldBe 2
                currentRedeemCount shouldBe 1
            }
    }

    private fun `쿠폰 발급 요청`(
        clientId: String,
        clientSecret: String,
        userId: String,
        couponTemplateId: Long,
    ): Response {
        val request = IssueCouponRequest(userId = userId)

        return given()
            .withApiKeyHeaders(clientId = clientId, clientSecret = clientSecret)
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/coupon-templates/${couponTemplateId}/issue")
    }

    private fun `쿠폰 사용 요청`(
        clientId: String,
        clientSecret: String,
        userCouponId: String,
        userId: String,
        orderAmount: Int,
        orderId: String,
    ): Response {
        val request = RedeemCouponRequest(
            userId = userId,
            orderAmount = orderAmount,
            orderId = orderId
        )

        return given()
            .withApiKeyHeaders(clientId = clientId, clientSecret = clientSecret)
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/user-coupons/${userCouponId}/redeem")
    }

    private fun `쿠폰 사용 취소 요청`(
        clientId: String,
        clientSecret: String,
        userCouponId: String,
        orderId: String,
        cancelReason: String,
    ): Response {
        val request = CancelCouponRedemptionRequest(
            cancelReason = cancelReason,
            orderId = orderId
        )

        return given()
            .withApiKeyHeaders(clientId = clientId, clientSecret = clientSecret)
            .contentType(ContentType.JSON)
            .body(request)
            .`when`()
            .post("/v1/user-coupons/${userCouponId}/cancel")
    }

    private fun `사용자가 보유한 쿠폰 목록 조회 요청`(
        clientId: String,
        clientSecret: String,
        userId: String,
    ): Response {
        return given()
            .withApiKeyHeaders(clientId = clientId, clientSecret = clientSecret)
            .queryParam("userId", userId)
            .`when`()
            .get("/v1/user-coupons")
    }

    private fun `사용자의 사용 가능한 쿠폰 목록 조회 요청`(
        clientId: String,
        clientSecret: String,
        userId: String,
        orderAmount: Int,
    ): Response {
        return given()
            .withApiKeyHeaders(clientId = clientId, clientSecret = clientSecret)
            .queryParam("userId", userId)
            .queryParam("orderAmount", orderAmount)
            .`when`()
            .get("/v1/user-coupons/available")
    }

    private fun `쿠폰 템플릿 상세 정보와 사용자에 따른 발급 가능 여부 조회 요청`(
        clientId: String,
        clientSecret: String,
        couponTemplateId: Long,
        userId: String? = null,
    ): Response {
        return given()
            .withApiKeyHeaders(clientId = clientId, clientSecret = clientSecret)
            .queryParam("userId", userId)
            .`when`()
            .get("/v1/coupon-templates/${couponTemplateId}")
    }
}
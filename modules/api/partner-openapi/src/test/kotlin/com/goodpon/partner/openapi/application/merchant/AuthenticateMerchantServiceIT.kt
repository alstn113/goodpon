package com.goodpon.partner.openapi.application.merchant

import com.goodpon.partner.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.partner.application.merchant.service.AuthenticateMerchantService
import com.goodpon.partner.application.merchant.service.exception.MerchantClientSecretMismatchException
import com.goodpon.partner.openapi.support.AbstractIntegrationTest
import com.goodpon.partner.openapi.support.accessor.TestMerchantAccessor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class AuthenticateMerchantServiceIT(
    private val authenticateMerchantService: AuthenticateMerchantService,
    private val testMerchantAccessor: TestMerchantAccessor,
) : AbstractIntegrationTest() {

    @Test
    fun `상점 인증을 수행하고 상점 정보를 반환한다`() {
        // given
        val (merchantId, clientId, clientSecret) = testMerchantAccessor.createMerchant()

        // when
        val merchantInfo = authenticateMerchantService(clientId = clientId, clientSecret = clientSecret)

        // then
        merchantInfo.id shouldBe merchantId
    }

    @Test
    fun `존재하지 않는 상점 인증 요청 시 예외가 발생한다`() {
        // given
        val nonExistentClientId = "non-existent-client-id"
        val nonExistentClientSecret = "non-existent-client-secret"

        // when, then
        shouldThrow<MerchantNotFoundException> {
            authenticateMerchantService(clientId = nonExistentClientId, clientSecret = nonExistentClientSecret)
        }
    }

    @Test
    fun `상점 인증 요청 시 클라이언트 ID와 비밀이 일치하지 않으면 예외가 발생한다`() {
        // given
        val (_, clientId, _) = testMerchantAccessor.createMerchant()
        val wrongClientSecret = "wrong-client-secret"

        // when, then
        shouldThrow<MerchantClientSecretMismatchException> {
            authenticateMerchantService(clientId = clientId, clientSecret = wrongClientSecret)
        }
    }
}
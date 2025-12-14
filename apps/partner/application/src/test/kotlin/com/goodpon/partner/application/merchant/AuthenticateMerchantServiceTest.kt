package com.goodpon.partner.application.merchant

import com.goodpon.domain.merchant.Merchant
import com.goodpon.partner.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.partner.application.merchant.service.AuthenticateMerchantService
import com.goodpon.partner.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.partner.application.merchant.service.exception.MerchantClientSecretMismatchException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class AuthenticateMerchantServiceTest : DescribeSpec({

    val merchantAccessor = mockk<MerchantAccessor>()
    val authenticateMerchantService = AuthenticateMerchantService(merchantAccessor)

    describe("AuthenticateMerchantService") {
        it("Client Id에 해당하는 상점을 찾지 못한 경우 예외를 던진다.") {
            val nonExistentClientId = "non-existent-client-id"
            val clientSecret = "client-secret"

            every { merchantAccessor.readByClientId(nonExistentClientId) } throws MerchantNotFoundException()

            shouldThrow<MerchantNotFoundException> {
                authenticateMerchantService(nonExistentClientId, clientSecret)
            }
        }

        it("상점 인증 요청 시 클라이언트 ID와 비밀이 일치하지 않으면 예외가 발생한다.") {
            val clientId = "valid-client-id"
            val wrongClientSecret = "wrong-client-secret"

            val merchant = mockk<Merchant>()
            every { merchant.isValidClientSecret(wrongClientSecret) } returns false
            every { merchantAccessor.readByClientId(clientId) } returns merchant

            shouldThrow<MerchantClientSecretMismatchException> {
                authenticateMerchantService(clientId, wrongClientSecret)
            }
        }
    }
})
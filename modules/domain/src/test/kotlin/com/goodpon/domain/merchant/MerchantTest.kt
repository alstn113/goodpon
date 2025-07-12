package com.goodpon.domain.merchant

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class MerchantTest : DescribeSpec({

    describe("MerchantAccount.create") {
        val name = "상점 이름"
        val accountId = 1L

        context("상점을 생성할 때") {
            val merchant = Merchant.create(
                name = name,
                accountId = accountId
            )

            it("상점 정보가 생성된다.") {
                merchant.name shouldBe name
                merchant.clientId.startsWith("ck_") shouldBe true
            }

            it("소유자 계정이 생성된다.") {
                merchant.accounts.size shouldBe 1
                val account = merchant.accounts.first()
                account.accountId shouldBe accountId
                account.role shouldBe MerchantAccountRole.OWNER
            }

            it("상점 비밀키가 생성된다.") {
                merchant.secrets.size shouldBe 1
                val secret = merchant.secrets.first()
                secret.expiredAt shouldBe null
                secret.secret.startsWith("sk_") shouldBe true
            }
        }
    }

    describe("Merchant.issueNewSecret") {
        val merchant = Merchant.create(name = "상점 이름", accountId = 1L)


        it("새로운 비밀키를 발급할 수 있다.") {
            val updatedMerchant = merchant.issueNewSecret()

            updatedMerchant.secrets.size shouldBe 2
            updatedMerchant.secrets.last().secret.startsWith("sk_") shouldBe true
        }
    }

    describe("Merchant.expireSecret") {
        val merchant = Merchant.create(name = "상점 이름", accountId = 1L)
        val secretId = merchant.secrets.first().id

        it("비밀키를 만료시킬 수 있다.") {
            val expiredAt = java.time.LocalDateTime.now()
            val updatedMerchant = merchant.expireSecret(secretId, expiredAt)

            updatedMerchant.secrets.size shouldBe 1
            updatedMerchant.secrets.first().expiredAt shouldBe expiredAt
        }
    }

    describe("Merchant.isValidClientSecret") {
        val merchant = Merchant.create(name = "상점 이름", accountId = 1L)
        val validSecret = merchant.secrets.first().secret

        it("비밀키가 유효한지 확인할 수 있다.") {
            merchant.isValidClientSecret(validSecret) shouldBe true
            merchant.isValidClientSecret("invalid_secret") shouldBe false
        }
    }

    describe("Merchant.isAccessibleBy") {
        val merchant = Merchant.create(name = "상점 이름", accountId = 1L)

        it("특정 계정이 상점에 접근 가능한지 확인할 수 있다.") {
            merchant.isAccessibleBy(1L) shouldBe true
            merchant.isAccessibleBy(2L) shouldBe false
        }
    }
})
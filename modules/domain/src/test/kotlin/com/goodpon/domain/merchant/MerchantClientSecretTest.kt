package com.goodpon.domain.merchant

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class MerchantClientSecretTest : DescribeSpec({

    describe("MerchantClientSecret.create") {
        it("상점 비밀키 생성 시 만료일은 존재하지 않는다.") {
            val secret = "test-secret"
            val merchantClientSecret = MerchantClientSecret.create(secret)

            merchantClientSecret.expiredAt shouldBe null
        }
    }

    describe("expire") {
        it("상점 비밀키를 만료시킬 수 있다.") {
            val secret = "test-secret"
            val merchantClientSecret = MerchantClientSecret.create(secret)

            val expiredAt = LocalDateTime.now()
            val expiredSecret = merchantClientSecret.expire(expiredAt)

            expiredSecret.expiredAt shouldBe expiredAt
        }
    }

    describe("isExpired") {
        it("상점 비밀키의 만료 여부를 확인할 수 있다.") {
            val secret = "test-secret"
            val merchantClientSecret = MerchantClientSecret.create(secret)

            merchantClientSecret.isExpired() shouldBe false

            val expiredAt = LocalDateTime.now()
            val expiredSecret = merchantClientSecret.expire(expiredAt)

            expiredSecret.isExpired() shouldBe true
        }
    }
})

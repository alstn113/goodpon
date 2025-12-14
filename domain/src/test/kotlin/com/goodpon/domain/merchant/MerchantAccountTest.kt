package com.goodpon.domain.merchant

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class MerchantAccountTest : DescribeSpec({

    describe("MerchantAccount.createOwner") {
        it("상점 소유자 계정을 생성할 수 있다.") {
            val accountId = 1L
            val merchantAccount = MerchantAccount.createOwner(accountId)

            merchantAccount.accountId shouldBe accountId
            merchantAccount.role shouldBe MerchantAccountRole.OWNER
        }
    }

    describe("MerchantAccount.createStaff") {
        it("상점 직원 계정을 생성할 수 있다.") {
            val accountId = 2L
            val merchantAccount = MerchantAccount.createStaff(accountId)

            merchantAccount.accountId shouldBe accountId
            merchantAccount.role shouldBe MerchantAccountRole.STAFF
        }
    }
})
package com.goodpon.domain.account

import com.goodpon.domain.domain.account.exception.AccountAlreadyVerifiedException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class AccountTest : DescribeSpec({

    describe("Account.create") {
        val email = "test@goodpon.site"
        val password = "testPassword123"
        val name = "testUser"

        it("계정을 생성할 수 있다.") {
            val account = Account.create(email, password, name)
            account.email.value shouldBe email
            account.password.value shouldBe password
            account.name.value shouldBe name
            account.verified shouldBe false
            account.verifiedAt shouldBe null
        }
    }

    describe("Account.verify") {
        it("미인증 계정은 인증 처리된다.") {
            val account = Account.create("test@goodpon.site", "testPassword123", "testUser")
            val now = LocalDateTime.now()

            val verifiedAccount = account.verify(now)

            verifiedAccount.verified shouldBe true
            verifiedAccount.verifiedAt shouldBe now
        }

        it("이미 인증된 계정은 인증 처리할 수 없다.") {
            val account = Account.create("test@goodpon.site", "testPassword123", "testUser")
            val now = LocalDateTime.now()
            val verifiedAccount = account.verify(now)

            shouldThrow<AccountAlreadyVerifiedException> {
                verifiedAccount.verify(now)
            }
        }
    }
})
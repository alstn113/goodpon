package com.goodpon.core.domain.account.vo

import com.goodpon.core.support.error.CoreException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class AccountPasswordTest : DescribeSpec({
    describe("계정 비밀번호는 8자 이상 100자 이하 문자열이어야 한다.") {
        forAll(
            row("a".repeat(8)),
            row("a".repeat(50)),
            row("a".repeat(100)),
        ) { password ->
            AccountPassword(password).value shouldBe password
        }
    }

    describe("계정 비밀번호는 8자 미만 또는 100자 초과인 경우 예외를 발생시킨다.") {
        forAll(
            row("a".repeat(7)),
            row("a".repeat(101)),
        ) { password ->
            val exception = shouldThrow<CoreException> { AccountPassword(password) }
            exception.errorType.message shouldBe "계정 비밀번호는 8자 이상 100자 이하여야 합니다."
            exception.errorType.statusCode shouldBe 400
        }
    }
})
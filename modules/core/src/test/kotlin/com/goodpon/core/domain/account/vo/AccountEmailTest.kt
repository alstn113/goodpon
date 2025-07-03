package com.goodpon.core.domain.account.vo

import com.goodpon.core.support.error.CoreException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class AccountEmailTest : DescribeSpec({
    describe("계정 이메일은 이메일 형식이어야 한다.") {
        forAll(
            row("test@goodpon.site"),
            row("A1.B2_C3@goodpon.site")
        ) { email ->
            AccountEmail(email).value shouldBe email
        }
    }

    describe("계정 이메일은 올바르지 않은 이메일 형식일 경우 예외를 발생시킨다.") {
        forAll(
            row("address"), // @ 없음
            row("address@goodpon"), // 도메인에 . 없음
            row("@goodpon.site"), // 로컬파트 없음
            row("address@goodpon.s"), // TLD 1자
        ) { email ->
            val exception = shouldThrow<CoreException> { AccountEmail(email) }
            exception.errorType.message shouldBe "올바르지 않은 계정 이메일 형식입니다."
            exception.errorType.statusCode shouldBe 400
        }
    }
})
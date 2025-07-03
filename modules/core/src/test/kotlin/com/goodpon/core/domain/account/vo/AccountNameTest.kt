package com.goodpon.core.domain.account.vo

import com.goodpon.core.support.error.CoreException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class AccountNameTest : DescribeSpec({

    describe("계정 이름은 1자 이상 50자 이하 문자열이어야 한다.") {
        forAll(
            row("a"),
            row("a".repeat(25)),
            row("a".repeat(50)),
        ) { name ->
            AccountName(name).value shouldBe name
        }
    }

    describe("계정 이름은 50자를 넘을 수 없다.") {
        val name = "a".repeat(51)
        val exception = shouldThrow<CoreException> { AccountName(name) }
        exception.errorType.message shouldBe "계정 이름은 50자 이하여야 합니다."
        exception.errorType.statusCode shouldBe 400
    }

    describe("계정 이름은 공백으로만 이루어질 수 없다.") {
        forAll(
            row(""),
            row(" "),
        ) { name ->
            val exception = shouldThrow<CoreException> { AccountName(name) }
            exception.errorType.message shouldBe "계정 이름은 공백으로만 이루어질 수 없습니다."
            exception.errorType.statusCode shouldBe 400
        }
    }
})

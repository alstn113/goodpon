package com.goodpon.domain.account.vo

import com.goodpon.domain.domain.account.exception.AccountInvalidNameLengthException
import com.goodpon.domain.domain.account.exception.AccountNameBlankException
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

        shouldThrow<AccountInvalidNameLengthException> { AccountName(name) }
    }

    describe("계정 이름은 공백으로만 이루어질 수 없다.") {
        forAll(
            row(""),
            row(" "),
        ) { name ->
            shouldThrow<AccountNameBlankException> { AccountName(name) }
        }
    }
})

package com.goodpon.domain.application.account.accessor

import com.goodpon.domain.domain.account.AccountRepository
import com.goodpon.domain.domain.account.exception.AccountNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class AccountReaderTest : DescribeSpec({

    val accountRepository = mockk<AccountRepository>()
    val accountReader = AccountReader(accountRepository)

    describe("readById") {
        it("존재하지 않는 경우 예외를 발생시킨다.") {
            every {
                accountRepository.findById(any())
            } returns null

            shouldThrow<AccountNotFoundException> {
                accountReader.readById(1L)
            }
        }
    }

    describe("readByEmail") {
        it("존재하지 않는 경우 예외를 발생시킨다.") {
            every {
                accountRepository.findByEmail(any())
            } returns null

            shouldThrow<AccountNotFoundException> {
                accountReader.readByEmail("notExists@gmail.com")
            }
        }
    }
})
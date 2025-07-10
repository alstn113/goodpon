package com.goodpon.dashboard.application.account.accessor

import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.dashboard.application.account.service.accessor.AccountReader
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class AccountReaderTest : DescribeSpec({

    val accountRepository = mockk<AccountRepository>()
    val accountReader = AccountReader(accountRepository)

    describe("readById") {
        it("계정 식별자로 계정을 조회할 수 없는 경우 예외를 발생시킨다.") {
            every {
                accountRepository.findById(any())
            } returns null

            shouldThrow<AccountNotFoundException> {
                accountReader.readById(1L)
            }
        }
    }

    describe("readByEmail") {
        it("계정 이메일로 계정을 조회할 수 없는 경우 예외를 발생시킨다.") {
            every {
                accountRepository.findByEmail(any())
            } returns null

            shouldThrow<AccountNotFoundException> {
                accountReader.readByEmail("notExists@gmail.com")
            }
        }
    }
})
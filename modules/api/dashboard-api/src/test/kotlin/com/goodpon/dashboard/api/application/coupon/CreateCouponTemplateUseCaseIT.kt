package com.goodpon.dashboard.api.application.coupon

import com.goodpon.dashboard.api.support.AbstractIntegrationTest
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.coupon.port.`in`.CreateCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.domain.account.Account
import com.goodpon.domain.coupon.template.exception.creation.CouponTemplateValidationError
import com.goodpon.domain.coupon.template.exception.creation.CouponTemplateValidationException
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.merchant.Merchant
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class CreateCouponTemplateUseCaseIT(
    private val accountRepository: AccountRepository,
    private val merchantRepository: MerchantRepository,
    private val couponTemplateRepository: CouponTemplateRepository,
    private val createCouponTemplateUseCase: CreateCouponTemplateUseCase,
) : AbstractIntegrationTest() {

    val command = CreateCouponTemplateCommand(
        accountId = 1L,
        name = "테스트 쿠폰 템플릿",
        description = "테스트 쿠폰 템플릿 설명",
        merchantId = 1L,
        minOrderAmount = 10000,
        discountType = CouponDiscountType.FIXED_AMOUNT,
        discountValue = 5000,
        maxDiscountAmount = null,
        issueStartDate = LocalDate.now(),
        issueEndDate = LocalDate.now().plusDays(10),
        validityDays = 3,
        absoluteExpiryDate = LocalDate.now().plusDays(30),
        limitType = CouponLimitPolicyType.ISSUE_COUNT,
        maxIssueCount = 2000L,
        maxRedeemCount = null
    )

    @Test
    fun `쿠폰 템플릿을 생성할 수 있다`() {
        // given
        val account = Account.create(email = "test@goodpon.site", password = "password", name = "테스트 계정")
            .verify(verifiedAt = LocalDateTime.now())
        val savedAccount = accountRepository.save(account)
        val merchant = Merchant.create(name = "테스트 상점", accountId = savedAccount.id)
        merchantRepository.save(merchant)

        // when
        val result = createCouponTemplateUseCase.createCouponTemplate(command)

        // then
        val foundCouponTemplate = couponTemplateRepository.findById(result.id)
        foundCouponTemplate.shouldNotBeNull()
        foundCouponTemplate.id shouldBe result.id
        foundCouponTemplate.status shouldBe CouponTemplateStatus.DRAFT
    }

    @Test
    fun `상점에 대한 접근 권한이 없는 경우 예외가 발생한다`() {
        // given
        val account = Account.create(email = "test@goodpon.site", password = "password", name = "테스트 계정")
            .verify(verifiedAt = LocalDateTime.now())
        val savedAccount = accountRepository.save(account)
        val merchant = Merchant.create(name = "테스트 상점", accountId = savedAccount.id)
        val savedMerchant = merchantRepository.save(merchant)

        val invalidAccountId = 2L
        val newCommand = command.copy(
            accountId = invalidAccountId,
            merchantId = savedMerchant.id
        )

        // when & then
        shouldThrow<NoMerchantAccessPermissionException> {
            createCouponTemplateUseCase.createCouponTemplate(newCommand)
        }
    }

    @Test
    fun `쿠폰 템플릿 생성을 위한 입력값이 잘못된 경우 예외가 발생한다`() {
        // given
        val account = Account.create(email = "test@goodpon.site", password = "password", name = "테스트 계정")
            .verify(verifiedAt = LocalDateTime.now())
        val savedAccount = accountRepository.save(account)
        val merchant = Merchant.create(name = "테스트 상점", accountId = savedAccount.id)
        val savedMerchant = merchantRepository.save(merchant)

        val invalidCommand = command.copy(
            accountId = savedAccount.id,
            merchantId = savedMerchant.id,
            minOrderAmount = 0, // 최소 주문 금액이 있을 경우 0보다 커야 함
            discountType = CouponDiscountType.PERCENTAGE,
            discountValue = 105, // 할인율은 0~100 사이여야 함
            maxDiscountAmount = 5000,
            issueStartDate = LocalDate.now().plusDays(5),
            issueEndDate = LocalDate.now().plusDays(3), // 종료일은 시작일보다 이후여야 함
            validityDays = 3,
            absoluteExpiryDate = LocalDate.now().minusDays(8),
            limitType = CouponLimitPolicyType.ISSUE_COUNT,
            maxIssueCount = 100L,
            maxRedeemCount = 200L // 발급 횟수 제한을 설정한 경우 사용 횟수 제한을 설정할 수 없음.
        )

        // when & then
        val exception = shouldThrow<CouponTemplateValidationException> {
            createCouponTemplateUseCase.createCouponTemplate(invalidCommand)
        }
        val expectedErrors = listOf(
            CouponTemplateValidationError(
                field = "minOrderAmount",
                rejectedValue = 0,
                message = "쿠폰 사용 조건의 최소 주문 금액은 0보다 커야 합니다."
            ),
            CouponTemplateValidationError(
                field = "discountValue",
                rejectedValue = 105,
                message = "백분율 할인 값은 1~100 사이여야 합니다."
            ),
            CouponTemplateValidationError(
                field = "issueEndDate",
                rejectedValue = LocalDate.now().plusDays(3),
                message = "쿠폰 발행 종료일은 발행 시작일보다 이전일 수 없습니다."
            ),
            CouponTemplateValidationError(
                field = "maxRedeemCount",
                rejectedValue = 200L,
                message = "발행 제한 정책이 설정된 쿠폰은 사용 제한 수량을 함께 설정할 수 없습니다."
            ),
        )
        exception.errors shouldBe expectedErrors
    }
}
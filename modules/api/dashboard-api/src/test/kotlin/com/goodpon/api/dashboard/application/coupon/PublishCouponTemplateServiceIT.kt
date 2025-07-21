package com.goodpon.api.dashboard.application.coupon

import com.goodpon.api.dashboard.support.AbstractIntegrationTest
import com.goodpon.application.dashboard.account.port.out.AccountRepository
import com.goodpon.application.dashboard.coupon.port.`in`.dto.PublishCouponTemplateCommand
import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateRepository
import com.goodpon.application.dashboard.coupon.service.PublishCouponTemplateService
import com.goodpon.application.dashboard.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.dashboard.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.application.dashboard.merchant.port.out.MerchantRepository
import com.goodpon.domain.account.Account
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.CouponTemplateFactory
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.merchant.Merchant
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class PublishCouponTemplateServiceIT(
    private val publishCouponTemplateService: PublishCouponTemplateService,
    private val accountRepository: AccountRepository,
    private val merchantRepository: MerchantRepository,
    private val couponTemplateRepository: CouponTemplateRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `쿠폰 템플릿을 발행할 수 있다`() {
        // given
        val savedAccount = createAndSaveVerifiedAccount()
        val savedMerchant = createAndSaveMerchant(accountId = savedAccount.id)
        val savedCouponTemplate = createAndSaveCouponTemplate(merchantId = savedMerchant.id)

        // when
        val command = PublishCouponTemplateCommand(
            accountId = savedAccount.id,
            merchantId = savedMerchant.id,
            couponTemplateId = savedCouponTemplate.id
        )
        val result = publishCouponTemplateService(command)

        // then
        val foundCouponTemplate = couponTemplateRepository.findById(result.id)
        foundCouponTemplate.shouldNotBeNull()
        foundCouponTemplate.id shouldBe result.id
        foundCouponTemplate.name shouldBe savedCouponTemplate.name
        foundCouponTemplate.status shouldBe CouponTemplateStatus.ISSUABLE
    }

    @Test
    fun `계정이 상점에 대한 접근 권한이 없으면 쿠폰 템플릿을 발행할 수 없다`() {
        // given
        val savedAccount = createAndSaveVerifiedAccount()
        val savedMerchant = createAndSaveMerchant(accountId = savedAccount.id)
        val savedCouponTemplate = createAndSaveCouponTemplate(merchantId = savedMerchant.id)

        // when
        val command = PublishCouponTemplateCommand(
            accountId = 999L,
            merchantId = savedMerchant.id,
            couponTemplateId = savedCouponTemplate.id
        )

        // then
        shouldThrow<NoMerchantAccessPermissionException> {
            publishCouponTemplateService(command)
        }
    }

    @Test
    fun `쿠폰 템플릿이 상점에 속하지 않으면 쿠폰 템플릿을 발행할 수 없다`() {
        // given
        val savedAccount = createAndSaveVerifiedAccount()
        val savedMerchant = createAndSaveMerchant(accountId = savedAccount.id)
        val savedCouponTemplate = createAndSaveCouponTemplate(merchantId = savedMerchant.id)
        val anotherMerchant = createAndSaveMerchant(accountId = savedAccount.id)

        // when
        val command = PublishCouponTemplateCommand(
            accountId = savedAccount.id,
            merchantId = anotherMerchant.id, // 다른 상점의 ID
            couponTemplateId = savedCouponTemplate.id
        )

        // then
        shouldThrow<CouponTemplateNotOwnedByMerchantException> {
            publishCouponTemplateService(command)
        }
    }

    private fun createAndSaveVerifiedAccount(): Account {
        val account = Account.create(
            email = "test@goodpon.site",
            password = "password",
            name = "테스트 계정"
        ).verify(verifiedAt = LocalDateTime.now())
        return accountRepository.save(account)
    }

    private fun createAndSaveMerchant(accountId: Long): Merchant {
        val merchant = Merchant.create(
            name = "테스트 상점",
            accountId = accountId
        )
        return merchantRepository.save(merchant)
    }

    private fun createAndSaveCouponTemplate(merchantId: Long): CouponTemplate {
        val couponTemplate = CouponTemplateFactory.create(
            merchantId = merchantId,
            name = "Test Coupon Template",
            description = "Test Description",
            minOrderAmount = 10000,
            discountType = CouponDiscountType.FIXED_AMOUNT,
            discountValue = 1000,
            maxDiscountAmount = null,
            issueStartDate = LocalDate.now(),
            issueEndDate = null,
            validityDays = null,
            absoluteExpiryDate = null,
            limitType = CouponLimitPolicyType.NONE,
            maxIssueCount = null,
            maxRedeemCount = null,
            status = CouponTemplateStatus.DRAFT,
        )
        return couponTemplateRepository.save(couponTemplate)
    }
}

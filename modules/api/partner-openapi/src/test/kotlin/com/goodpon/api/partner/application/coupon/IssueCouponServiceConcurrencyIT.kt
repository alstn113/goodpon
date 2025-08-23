package com.goodpon.api.partner.application.coupon

import com.goodpon.api.partner.support.AbstractIntegrationTest
import com.goodpon.api.partner.support.accessor.TestCouponTemplateAccessor
import com.goodpon.api.partner.support.accessor.TestMerchantAccessor
import com.goodpon.application.partner.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.service.IssueCouponService
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class IssueCouponServiceConcurrencyIT(
    private val issueCouponService: IssueCouponService,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
) : AbstractIntegrationTest() {

    @Test
    fun `여러 사용자가 동시에 쿠폰 발급 요청 시 최대 발급 수량을 초과하지 않는다`(): Unit = runBlocking {
        val maxIssueCount = 10L
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(
            merchantId = merchantId,
            limitType = CouponLimitPolicyType.ISSUE_COUNT,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = null,
        )

        val userCounts = 20

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        coroutineScope {
            val jobs = List(userCounts) { userIndex ->
                launch(Dispatchers.IO) {
                    try {
                        val command = IssueCouponCommand(
                            merchantId = merchantId,
                            couponTemplateId = couponTemplateId,
                            userId = "user-$userIndex"
                        )
                        issueCouponService(command)
                        successCount.incrementAndGet()
                    } catch (e: CouponTemplateIssuanceLimitExceededException) {
                        failureCount.incrementAndGet()
                    }
                }
            }
            jobs.joinAll()
        }

        successCount.get() shouldBe maxIssueCount
        failureCount.get() shouldBe userCounts - maxIssueCount

        couponTemplateStatsCache.getStats(couponTemplateId).first shouldBe maxIssueCount
    }

    @Test
    fun `동일한 사용자가 동일한 쿠폰을 동시에 발급 요청 시 하나만 허용된다`(): Unit = runBlocking {
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(merchantId = merchantId)
        val userId = "user-unique"
        val concurrentRequests = 5

        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        coroutineScope {
            val jobs = List(concurrentRequests) { _ ->
                launch(Dispatchers.IO) {
                    try {
                        val command = IssueCouponCommand(
                            merchantId = merchantId,
                            couponTemplateId = couponTemplateId,
                            userId = userId
                        )
                        issueCouponService(command)
                        successCount.incrementAndGet()
                    } catch (e: Exception) {
                        failureCount.incrementAndGet()
                    }
                }
            }
            jobs.joinAll()
        }

        successCount.get() shouldBe 1
        failureCount.get() shouldBe concurrentRequests - 1

        couponTemplateStatsCache.getStats(couponTemplateId).let {
            it.first shouldBe 1L
            it.second shouldBe 0L
        }
    }
}

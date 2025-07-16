package com.goodpon.partner.openapi.application

import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import com.goodpon.partner.application.coupon.port.`in`.dto.IssueCouponCommand
import com.goodpon.partner.application.coupon.service.IssueCouponService
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.partner.openapi.support.AbstractIntegrationTest
import com.goodpon.partner.openapi.support.accessor.TestCouponHistoryAccessor
import com.goodpon.partner.openapi.support.accessor.TestCouponTemplateStatsAccessor
import com.goodpon.partner.openapi.support.accessor.TestUserCouponAccessor
import io.kotest.common.runBlocking
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import java.util.concurrent.atomic.AtomicInteger

@Sql("/sql/init-coupon-template.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class IssueCouponServiceConcurrencyIT(
    private val issueCouponService: IssueCouponService,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val testCouponTemplateStatsAccessor: TestCouponTemplateStatsAccessor,
    private val testCouponHistoryAccessor: TestCouponHistoryAccessor,
) : AbstractIntegrationTest() {

    @Test
    fun `여러 사용자가 동시에 쿠폰 발급 시 발급 제한을 초과하지 않는다`(): Unit = runBlocking {
        val merchantId = 10L
        val couponTemplateId = 100L
        val userCounts = 20
        val maxIssueCount = 10

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
                        issueCouponService.issueCoupon(command)
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

        val foundUserCoupons = testUserCouponAccessor.findByCouponTemplateId(couponTemplateId)
        foundUserCoupons.size shouldBe maxIssueCount

        val foundStats = testCouponTemplateStatsAccessor.findById(couponTemplateId)
        foundStats.shouldNotBeNull()
        foundStats.issueCount shouldBe maxIssueCount

        val foundHistories = testCouponHistoryAccessor.findAll()
        foundHistories.size shouldBe maxIssueCount
    }

    @Test
    fun `동일한 사용자가 쿠폰을 동시에 발급 시 하나만 발급된다`(): Unit = runBlocking {
        val merchantId = 10L
        val couponTemplateId = 100L
        val userId = "user-unique"
        val concurrentRequests = 10

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
                        issueCouponService.issueCoupon(command)
                        successCount.incrementAndGet()
                    } catch (e: UserCouponAlreadyIssuedException) {
                        failureCount.incrementAndGet()
                    }
                }
            }
            jobs.joinAll()
        }

        successCount.get() shouldBe 1
        failureCount.get() shouldBe concurrentRequests - 1

        val foundUserCoupons = testUserCouponAccessor.findByCouponTemplateId(couponTemplateId)
        foundUserCoupons.size shouldBe 1
        foundUserCoupons.first().userId shouldBe userId

        val foundStats = testCouponTemplateStatsAccessor.findById(couponTemplateId)
        foundStats.shouldNotBeNull()
        foundStats.issueCount shouldBe 1

        val foundHistories = testCouponHistoryAccessor.findAll()
        foundHistories.size shouldBe 1
    }
}

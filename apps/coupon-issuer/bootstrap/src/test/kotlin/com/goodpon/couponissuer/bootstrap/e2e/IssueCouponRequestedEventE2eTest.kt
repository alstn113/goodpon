package com.goodpon.couponissuer.bootstrap.e2e

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.couponissuer.bootstrap.support.AbstractE2eTest
import com.goodpon.couponissuer.bootstrap.support.accessor.TestCouponTemplateAccessor
import com.goodpon.couponissuer.bootstrap.support.accessor.TestMerchantAccessor
import com.goodpon.couponissuer.bootstrap.support.accessor.TestUserCouponAccessor
import com.goodpon.couponissuer.worker.dto.IssueCouponRequestedEvent
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.redis.coupon.CouponIssueRedisStore
import com.goodpon.infra.redis.coupon.CouponStatsRedisStore
import io.kotest.matchers.shouldBe
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.springframework.kafka.core.KafkaTemplate
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit


class IssueCouponRequestedEventE2eTest(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val testMerchantAccessor: TestMerchantAccessor,
    private val testCouponTemplateAccessor: TestCouponTemplateAccessor,
    private val testUserCouponAccessor: TestUserCouponAccessor,
    private val couponIssueStore: CouponIssueRedisStore,
    private val couponStatsStore: CouponStatsRedisStore,
    private val objectMapper: ObjectMapper,
) : AbstractE2eTest() {

    @Test
    fun `쿠폰_발급_요청_이벤트를_처리한다`() {
        // given
        val (merchantId) = testMerchantAccessor.createMerchant()
        val couponTemplateId = testCouponTemplateAccessor.createCouponTemplate(merchantId = merchantId)
        val userId = "unique-user-id"
        couponIssueStore.reserve(
            couponTemplateId = couponTemplateId,
            userId = userId,
            maxIssueCount = 10,
        )

        val event = IssueCouponRequestedEvent(
            couponTemplateId = couponTemplateId,
            userId = userId,
            requestedAt = LocalDateTime.now()
        )
        val eventJsonString = objectMapper.writeValueAsString(event)

        // when
        kafkaTemplate.send("issue-coupon-requested", eventJsonString)

        // then
        await()
            .pollInterval(Duration.ofSeconds(1))
            .atMost(3, TimeUnit.SECONDS)
            .untilAsserted {
                val foundCoupon = testUserCouponAccessor.findAll().first()
                foundCoupon.couponTemplateId shouldBe couponTemplateId
                foundCoupon.userId shouldBe userId
                foundCoupon.status shouldBe UserCouponStatus.ISSUED

                val stats = couponStatsStore.getStats(couponTemplateId)
                stats.first shouldBe 1
            }
    }
}
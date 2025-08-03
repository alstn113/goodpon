package com.goodpon.infra.redis.idempotency

import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.infra.redis.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class IdempotencyRedisCacheAdapterIT(
    private val idempotencyRedisCacheAdapter: IdempotencyRedisCacheAdapter,
) : AbstractIntegrationTest() {

    @Test
    fun `멱등 요청 조회 시 존재하지 않는 경우 NotExists를 반환한다`() {
        // given
        val key = "unique-key"
        val requestHash = "request-body-hash"

        // when
        val result = idempotencyRedisCacheAdapter.check(key, requestHash)

        // then
        result shouldBe IdempotencyCheckResult.NotExists
    }

    @Test
    fun `처리 중인 멱등 요청을 저장할 수 있다`() {
        // given
        val key = "unique-key"
        val requestHash = "request-body-hash"

        // when
        idempotencyRedisCacheAdapter.saveProcessing(key, requestHash)

        // then
        val checkResult = idempotencyRedisCacheAdapter.check(key, requestHash)
        checkResult shouldBe IdempotencyCheckResult.Processing
    }

    @Test
    fun `완료된 멱등 요청을 저장할 수 있다`() {
        // given
        val key = "unique-key"
        val requestHash = "request-body-hash"
        idempotencyRedisCacheAdapter.saveProcessing(key, requestHash)

        // when
        val result = "result-data"
        idempotencyRedisCacheAdapter.saveCompleted(key, result)

        // then
        val checkResult = idempotencyRedisCacheAdapter.check(key, requestHash)
        checkResult shouldBe IdempotencyCheckResult.Completed(result)
    }

    @Test
    fun `완료된 멱등 요청 조회 시 다른 requestHash가 Conflict를 반환한다`() {
        // given
        val key = "unique-key"
        val requestHash1 = "request-body-hash-1"
        idempotencyRedisCacheAdapter.saveProcessing(key, requestHash1)
        idempotencyRedisCacheAdapter.saveCompleted(key, "result-data")

        // when
        val requestHash2 = "request-body-hash-2"
        val result = idempotencyRedisCacheAdapter.check(key, requestHash2)

        // then
        result shouldBe IdempotencyCheckResult.Conflict
    }
}
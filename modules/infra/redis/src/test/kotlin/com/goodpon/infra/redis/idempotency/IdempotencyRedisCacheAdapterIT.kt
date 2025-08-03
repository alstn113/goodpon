package com.goodpon.infra.redis.idempotency

import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse
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
        val result = idempotencyRedisCacheAdapter.validateKey(key, requestHash)

        // then
        result shouldBe IdempotencyCheckResult.NotFound
    }

    @Test
    fun `처리 중인 멱등 요청을 저장할 수 있다`() {
        // given
        val key = "unique-key"
        val requestHash = "request-body-hash"

        // when
        idempotencyRedisCacheAdapter.markAsProcessing(key, requestHash)

        // then
        val checkResult = idempotencyRedisCacheAdapter.validateKey(key, requestHash)
        checkResult shouldBe IdempotencyCheckResult.Processing
    }

    @Test
    fun `완료된 멱등 요청을 저장할 수 있다`() {
        // given
        val key = "unique-key"
        val requestHash = "request-body-hash"
        idempotencyRedisCacheAdapter.markAsProcessing(key, requestHash)

        // when
        val response = IdempotencyResponse(
            headers = mapOf("Content-Type" to listOf("application/json")),
            body = JsonNodeFactory.instance.objectNode().apply {
                put("key", "value")
            },
            status = 200
        )
        idempotencyRedisCacheAdapter.markAsCompleted(key, result)

        // then
        val checkResult = idempotencyRedisCacheAdapter.validateKey(key, requestHash)
        checkResult shouldBe IdempotencyCheckResult.Completed(result)
    }

    @Test
    fun `완료된 멱등 요청 조회 시 다른 requestHash가 Conflict를 반환한다`() {
        // given
        val key = "unique-key"
        val requestHash1 = "request-body-hash-1"
        idempotencyRedisCacheAdapter.markAsProcessing(key, requestHash1)
        idempotencyRedisCacheAdapter.markAsCompleted(key, "result-data")

        // when
        val requestHash2 = "request-body-hash-2"
        val result = idempotencyRedisCacheAdapter.validateKey(key, requestHash2)

        // then
        result shouldBe IdempotencyCheckResult.Conflict
    }
}
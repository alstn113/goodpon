package com.goodpon.infra.redis.idempotency

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.application.partner.idempotency.service.IdempotencyCheckResult
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse
import com.goodpon.infra.redis.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class IdempotencyRedisCacheAdapterIT(
    private val idempotencyRedisCacheAdapter: IdempotencyRedisCacheAdapter,
) : AbstractIntegrationTest() {

    private val key = "idempotency-key"
    private val requestHash = "request-hash"
    private val objectMapper = ObjectMapper()

    @Test
    fun `멱등성 키가 없으면 NotFound`() {
        // when
        val result = idempotencyRedisCacheAdapter.validateKey(key, requestHash);

        // then
        result shouldBe IdempotencyCheckResult.NotFound
    }

    @Test
    fun `markAsProcessing 이후 validateKey 결과는 Processing`() {
        // given
        idempotencyRedisCacheAdapter.markAsProcessing(key, requestHash)

        // when
        val result = idempotencyRedisCacheAdapter.validateKey(key, requestHash)

        // then
        result shouldBe IdempotencyCheckResult.Processing
    }

    @Test
    fun `markAsProcessing 이후 다른 hash로 validateKey 하면 Conflict`() {
        // given
        idempotencyRedisCacheAdapter.markAsProcessing(key, requestHash)
        idempotencyRedisCacheAdapter.markAsCompleted(
            key, IdempotencyResponse(
                status = 200,
                body = objectMapper.readTree("""{"message": "success"}"""),
                headers = mapOf("Content-Type" to listOf("application/json"))
            )
        )

        // when
        val differentHash = "different-hash"
        val result = idempotencyRedisCacheAdapter.validateKey(key, differentHash)

        // then
        result shouldBe IdempotencyCheckResult.Conflict
    }

    @Test
    fun `markAsCompleted 이후 validateKey 결과는 Completed`() {
        // given
        idempotencyRedisCacheAdapter.markAsProcessing(key, requestHash)

        // when
        val jsonNode = objectMapper.readTree("""{"message": "success"}""")
        val response = IdempotencyResponse(
            status = 200,
            body = jsonNode,
            headers = mapOf("Content-Type" to listOf("application/json"))
        )
        idempotencyRedisCacheAdapter.markAsCompleted(key, response)

        // then
        val result = idempotencyRedisCacheAdapter.validateKey(key, requestHash)
        result shouldBe IdempotencyCheckResult.Completed(response)
    }
}
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
    fun `멱등성 키가 없으면 FirstRequestProcessing`() {
        // when
        val result = idempotencyRedisCacheAdapter.checkOrMarkAsProcessing(key, requestHash)

        // then
        result shouldBe IdempotencyCheckResult.FirstRequestProcessing
    }

    @Test
    fun `checkOrMarkAsProcessing 이후 checkOrMarkAsProcessing 결과는 FirstRequestProcessing`() {
        // given
        idempotencyRedisCacheAdapter.checkOrMarkAsProcessing(key, requestHash)

        // when
        val result = idempotencyRedisCacheAdapter.checkOrMarkAsProcessing(key, requestHash)

        // then
        result shouldBe IdempotencyCheckResult.CurrentlyProcessing
    }

    @Test
    fun `checkOrMarkAsProcessing 이후 다른 hash로 checkOrMarkAsProcessing 하면 RequestBodyMismatch`() {
        // given
        idempotencyRedisCacheAdapter.checkOrMarkAsProcessing(key, requestHash)
        idempotencyRedisCacheAdapter.markAsCompleted(
            key, IdempotencyResponse(
                status = 200,
                body = objectMapper.readTree("""{"message": "success"}"""),
                headers = mapOf("Content-Type" to listOf("application/json"))
            )
        )

        // when
        val differentHash = "different-hash"
        val result = idempotencyRedisCacheAdapter.checkOrMarkAsProcessing(key, differentHash)

        // then
        result shouldBe IdempotencyCheckResult.RequestBodyMismatch
    }

    @Test
    fun `markAsCompleted 이후 checkOrMarkAsProcessing 결과는 AlreadyCompleted`() {
        // given
        idempotencyRedisCacheAdapter.checkOrMarkAsProcessing(key, requestHash)

        // when
        val jsonNode = objectMapper.readTree("""{"message": "success"}""")
        val response = IdempotencyResponse(
            status = 200,
            body = jsonNode,
            headers = mapOf("Content-Type" to listOf("application/json"))
        )
        idempotencyRedisCacheAdapter.markAsCompleted(key, response)

        // then
        val result = idempotencyRedisCacheAdapter.checkOrMarkAsProcessing(key, requestHash)
        result shouldBe IdempotencyCheckResult.AlreadyCompleted(response)
    }
}
package com.goodpon.partner.infra.idempotency

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.partner.application.idempotency.service.IdempotencyCheckResult
import com.goodpon.partner.application.idempotency.service.IdempotencyResponse
import com.goodpon.partner.infra.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class IdempotencyRedisStoreAdapterIT(
    private val idempotencyRedisStoreAdapter: IdempotencyRedisStoreAdapter,
) : AbstractIntegrationTest() {

    private val key = "idempotency-key"
    private val requestHash = "request-hash"
    private val objectMapper = ObjectMapper()

    @Test
    fun `멱등성 키가 없으면 FirstRequestProcessing`() {
        // when
        val result = idempotencyRedisStoreAdapter.checkOrMarkAsProcessing(key, requestHash)

        // then
        result shouldBe IdempotencyCheckResult.FirstRequestProcessing
    }

    @Test
    fun `checkOrMarkAsProcessing 이후 checkOrMarkAsProcessing 결과는 FirstRequestProcessing`() {
        // given
        idempotencyRedisStoreAdapter.checkOrMarkAsProcessing(key, requestHash)

        // when
        val result = idempotencyRedisStoreAdapter.checkOrMarkAsProcessing(key, requestHash)

        // then
        result shouldBe IdempotencyCheckResult.CurrentlyProcessing
    }

    @Test
    fun `checkOrMarkAsProcessing 이후 다른 hash로 checkOrMarkAsProcessing 하면 RequestBodyMismatch`() {
        // given
        idempotencyRedisStoreAdapter.checkOrMarkAsProcessing(key, requestHash)
        idempotencyRedisStoreAdapter.markAsCompleted(
            key, IdempotencyResponse(
                status = 200,
                body = objectMapper.readTree("""{"message": "success"}"""),
                headers = mapOf("Content-Type" to listOf("application/json"))
            )
        )

        // when
        val differentHash = "different-hash"
        val result = idempotencyRedisStoreAdapter.checkOrMarkAsProcessing(key, differentHash)

        // then
        result shouldBe IdempotencyCheckResult.RequestBodyMismatch
    }

    @Test
    fun `markAsCompleted 이후 checkOrMarkAsProcessing 결과는 AlreadyCompleted`() {
        // given
        idempotencyRedisStoreAdapter.checkOrMarkAsProcessing(key, requestHash)

        // when
        val jsonNode = objectMapper.readTree("""{"message": "success"}""")
        val response = IdempotencyResponse(
            status = 200,
            body = jsonNode,
            headers = mapOf("Content-Type" to listOf("application/json"))
        )
        idempotencyRedisStoreAdapter.markAsCompleted(key, response)

        // then
        val result = idempotencyRedisStoreAdapter.checkOrMarkAsProcessing(key, requestHash)
        result shouldBe IdempotencyCheckResult.AlreadyCompleted(response)
    }
}
package com.goodpon.infra.redis

import com.goodpon.domain.auth.EmailVerification
import com.goodpon.infra.redis.support.AbstractRedisIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDateTime

class EmailVerificationRedisAdapterIT(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val emailVerificationRedisAdapter: EmailVerificationRedisAdapter,
) : AbstractRedisIntegrationTest() {

    @Test
    fun `이메일 인증 정보를 저장할 수 있다`() {
        // given
        val token = "test-token"
        val accountId = 1L
        val emailVerification = createEmailVerification(accountId = accountId, token = token)

        // when
        emailVerificationRedisAdapter.save(
            verification = emailVerification,
            ttlMinutes = 30
        )

        // then
        val foundToken = redisTemplate.opsForValue().get("email-verification:accountId:$accountId") as String?
        val foundEmailVerification = redisTemplate.opsForValue()
            .get("email-verification:token:$token") as EmailVerification?

        foundToken shouldBe token
        foundEmailVerification shouldBe emailVerification
    }

    @Test
    fun `이메일 인증 정보를 저장할 때 이전 토큰을 삭제한다`() {
        // given
        val accountId = 1L
        val prevToken = "prev-token"
        val prevEmailVerification = createEmailVerification(accountId = accountId, token = prevToken)

        emailVerificationRedisAdapter.save(verification = prevEmailVerification, ttlMinutes = 30)

        // when
        val nextToken = "next-token"
        val nextEmailVerification = createEmailVerification(accountId = accountId, token = nextToken)

        emailVerificationRedisAdapter.save(verification = nextEmailVerification, ttlMinutes = 30)

        // then
        val foundPrevToken = redisTemplate.opsForValue()
            .get("email-verification:token:$prevToken") as EmailVerification?
        val foundNextToken = redisTemplate.opsForValue()
            .get("email-verification:token:$nextToken") as EmailVerification?

        foundPrevToken shouldBe null
        foundNextToken shouldBe nextEmailVerification
    }

    @Test
    fun `토큰으로 이메일 인증 정보를 조회할 수 있다`() {
        // given
        val token = "test-token"
        val accountId = 1L
        val emailVerification = createEmailVerification(accountId = accountId, token = token)

        emailVerificationRedisAdapter.save(verification = emailVerification, ttlMinutes = 30)

        // when
        val foundEmailVerification = emailVerificationRedisAdapter.findByToken(token)

        // then
        foundEmailVerification shouldBe emailVerification
    }

    @Test
    fun `존재하지 않는 토큰으로 이메일 인증 정보를 조회할 경우 null을 반환한다`() {
        // given
        val nonExistentToken = "non-existent"

        // when
        val foundEmailVerification = emailVerificationRedisAdapter.findByToken(nonExistentToken)

        // then
        foundEmailVerification shouldBe null
    }

    @Test
    fun `이메일 인증 정보를 삭제할 수 있다`() {
        // given
        val token = "test-token"
        val accountId = 1L
        val emailVerification = createEmailVerification(accountId = accountId, token = token)

        emailVerificationRedisAdapter.save(verification = emailVerification, ttlMinutes = 30)

        // when
        emailVerificationRedisAdapter.delete(token = token, accountId = accountId)

        // then
        val foundToken = redisTemplate.opsForValue().get("email-verification:accountId:$accountId")
        val foundEmailVerification = redisTemplate.opsForValue()
            .get("email-verification:token:$token") as EmailVerification?

        foundToken shouldBe null
        foundEmailVerification shouldBe null
    }

    private fun createEmailVerification(accountId: Long, token: String): EmailVerification {
        return EmailVerification(
            accountId = accountId,
            token = token,
            email = "email@goodpon.site",
            name = "name",
            createdAt = LocalDateTime.of(2025, 7, 7, 12, 0)
        )
    }
}

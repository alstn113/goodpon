package com.goodpon.infra.redis

import com.goodpon.dashboard.application.auth.port.out.EmailVerificationRepository
import com.goodpon.domain.auth.EmailVerification
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class EmailVerificationRedisAdapter(
    private val redisTemplate: RedisTemplate<String, Any>,
) : EmailVerificationRepository {

    override fun save(verification: EmailVerification, ttlMinutes: Long) {
        val tokenKey = buildTokenKey(verification.token)
        val accountIdKey = buildAccountIdKey(verification.accountId)

        deletePrevToken(accountIdKey)

        redisTemplate.opsForValue().set(
            accountIdKey,
            verification.token,
            Duration.ofMinutes(ttlMinutes)
        )
        redisTemplate.opsForValue().set(
            tokenKey,
            verification,
            Duration.ofMinutes(ttlMinutes)
        )
    }

    override fun findByToken(token: String): EmailVerification? {
        val tokenKey = buildTokenKey(token)
        return redisTemplate.opsForValue().get(tokenKey) as EmailVerification?
    }

    override fun delete(token: String, accountId: Long) {
        val tokenKey = buildTokenKey(token)
        val accountIdKey = buildAccountIdKey(accountId)

        redisTemplate.delete(tokenKey)
        redisTemplate.delete(accountIdKey)
    }

    private fun buildTokenKey(token: String): String {
        return "email-verification:token:$token"
    }

    private fun buildAccountIdKey(accountId: Long): String {
        return "email-verification:accountId:$accountId"
    }

    private fun deletePrevToken(accountIdKey: String) {
        val prevToken = redisTemplate.opsForValue().get(accountIdKey) as String?
        if (!prevToken.isNullOrBlank()) {
            redisTemplate.delete(buildTokenKey(prevToken))
        }
    }
}
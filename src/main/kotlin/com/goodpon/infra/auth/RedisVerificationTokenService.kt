package com.goodpon.infra.auth

import com.goodpon.goodpon.domain.auth.VerificationTokenService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class RedisVerificationTokenService(
    private val redisTemplate: RedisTemplate<String, Any>,
) : VerificationTokenService {

    companion object {
        private val TOKEN_TTL: Duration = Duration.ofMinutes(10)
    }

    override fun generateToken(accountId: Long): String {
        invalidateExistingToken(accountId)

        val newToken = generateToken()
        storeToken(accountId, newToken)

        return newToken
    }

    override fun validateAndConsumeToken(token: String): Long {
        val accountId = getAccountIdByToken(token)
        invalidateToken(token, accountId)
        return accountId
    }

    private fun invalidateExistingToken(accountId: Long) {
        val existingToken = redisTemplate.opsForValue().get(accountIdKey(accountId)) as String?
        existingToken?.let { redisTemplate.delete(tokenKey(it)) }
    }

    private fun generateToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun storeToken(accountId: Long, token: String) {
        redisTemplate.opsForValue().set(accountIdKey(accountId), token, TOKEN_TTL)
        redisTemplate.opsForValue().set(tokenKey(token), accountId, TOKEN_TTL)
    }

    private fun getAccountIdByToken(token: String): Long {
        return redisTemplate.opsForValue().get(tokenKey(token)) as Long?
            ?: throw IllegalArgumentException("Invalid or expired token")
    }

    private fun invalidateToken(token: String, accountId: Long) {
        redisTemplate.delete(tokenKey(token))
        redisTemplate.delete(accountIdKey(accountId))
    }

    private fun accountIdKey(accountId: Long) = "email-verification:account:$accountId"
    private fun tokenKey(token: String) = "email-verification:token:$token"
}

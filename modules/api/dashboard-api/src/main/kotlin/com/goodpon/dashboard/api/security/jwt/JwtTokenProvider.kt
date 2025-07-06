package com.goodpon.dashboard.api.security.jwt

import com.goodpon.dashboard.api.security.jwt.exception.BlankTokenException
import com.goodpon.dashboard.api.security.jwt.exception.InvalidTokenException
import com.goodpon.dashboard.api.security.jwt.exception.TokenExpiredException
import com.goodpon.dashboard.application.auth.port.out.TokenProvider
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
@EnableConfigurationProperties(JwtTokenProperties::class)
class JwtTokenProvider(
    private val properties: JwtTokenProperties,
) : TokenProvider {

    private val accessTokenSecretKey = Keys.hmacShaKeyFor(properties.accessTokenSecretKey.toByteArray())
    private val accessTokenExpirationTime = properties.accessTokenExpirationTime

    override fun generateAccessToken(accountId: Long): String {
        val now = Date()
        val expiration = Date(now.time + accessTokenExpirationTime)

        return Jwts.builder()
            .subject(accountId.toString())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(accessTokenSecretKey)
            .compact()
    }

    override fun getAccountId(accessToken: String): Long {
        val claims = toClaims(accessToken)
        val accountIdString = claims.subject

        return accountIdString.toLong()
    }

    private fun toClaims(token: String): Claims {
        if (token.isBlank()) {
            throw BlankTokenException()
        }

        try {
            val claimsJws: Jws<Claims> = getClaimsJws(token)

            return claimsJws.payload
        } catch (e: ExpiredJwtException) {
            throw TokenExpiredException(e)
        } catch (e: JwtException) {
            throw InvalidTokenException(e)
        }
    }

    private fun getClaimsJws(token: String): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(accessTokenSecretKey)
            .build()
            .parseSignedClaims(token)
    }
}
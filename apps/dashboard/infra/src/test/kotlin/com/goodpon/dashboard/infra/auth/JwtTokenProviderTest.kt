package com.goodpon.dashboard.infra.auth

import com.goodpon.dashboard.application.auth.port.out.exception.BlankTokenException
import com.goodpon.dashboard.application.auth.port.out.exception.InvalidTokenException
import com.goodpon.dashboard.application.auth.port.out.exception.TokenExpiredException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank

class JwtTokenProviderTest : DescribeSpec({

    val testSecretKey = "thisisalongsecretkeyforjwttokenproviderfortestingpurpose"
    val testExpirationTime = 24 * 60 * 60 * 1000L // 1일
    val properties = JwtTokenProperties(
        JwtTokenProperties.AccessToken(
            secretKey = testSecretKey,
            expirationTime = testExpirationTime
        )
    )
    val jwtTokenProvider = JwtTokenProvider(properties)

    describe("JwtTokenProvider") {

        context("generateAccessToken 메서드는") {
            it("유효한 JWT를 생성해야 한다") {
                val accountId = 1L
                val token = jwtTokenProvider.generateAccessToken(accountId)

                token.shouldNotBeBlank()

                val claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(testSecretKey.toByteArray()))
                    .build()
                    .parseSignedClaims(token)
                    .payload

                claims.subject shouldBe accountId.toString()
            }
        }

        context("getAccountId 메서드는") {
            it("유효한 토큰으로부터 accountId를 추출해야 한다") {
                val accountId = 123L
                val token = jwtTokenProvider.generateAccessToken(accountId)
                val extractedAccountId = jwtTokenProvider.getAccountId(token)

                extractedAccountId shouldBe accountId
            }

            it("만료된 토큰인 경우 예외를 던진다") {
                val expiredProperties = JwtTokenProperties(
                    JwtTokenProperties.AccessToken(
                        secretKey = testSecretKey,
                        expirationTime = 1L // (1ms)
                    )
                )
                val expiredJwtTokenProvider = JwtTokenProvider(expiredProperties)

                val accountId = 456L
                val expiredToken = expiredJwtTokenProvider.generateAccessToken(accountId)

                Thread.sleep(100)

                shouldThrow<TokenExpiredException> {
                    jwtTokenProvider.getAccountId(expiredToken)
                }
            }

            it("유효하지 않은 시크릿 키로 서명된 토큰인 경우 예외를 던진다") {
                val otherSecretKey = "anothersecretkeyforjwttokenproviderthatisdifferent"
                val wrongJwtTokenProvider = JwtTokenProperties(
                    JwtTokenProperties.AccessToken(
                        secretKey = otherSecretKey,
                        expirationTime = testExpirationTime
                    )
                )
                val accountId = 789L
                val invalidlySignedToken = JwtTokenProvider(wrongJwtTokenProvider).generateAccessToken(accountId)

                shouldThrow<InvalidTokenException> {
                    jwtTokenProvider.getAccountId(invalidlySignedToken)
                }
            }

            it("손상된 형식의 토큰인 경우 예외를 던진다") {
                val malformedToken = "this.is.not.a.valid.jwt.token"

                shouldThrow<InvalidTokenException> {
                    jwtTokenProvider.getAccountId(malformedToken)
                }
            }

            it("빈 문자열 토큰인 경우 예외를 던진다") {
                shouldThrow<BlankTokenException> {
                    jwtTokenProvider.getAccountId("")
                }
            }

            it("공백 문자열 토큰인 경우 예외를 던진다") {
                shouldThrow<BlankTokenException> {
                    jwtTokenProvider.getAccountId("   ")
                }
            }
        }
    }
})
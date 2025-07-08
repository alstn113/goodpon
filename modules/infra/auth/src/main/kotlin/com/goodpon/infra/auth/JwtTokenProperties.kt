package com.goodpon.infra.auth

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("security.jwt")
data class JwtTokenProperties(
    val accessToken: AccessToken,
) {

    data class AccessToken(
        val secretKey: String,
        val expirationTime: Long,
    )
}
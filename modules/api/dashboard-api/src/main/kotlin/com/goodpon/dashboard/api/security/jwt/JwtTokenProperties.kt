package com.goodpon.dashboard.api.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("security.jwt")
data class JwtTokenProperties(
    val accessTokenSecretKey: String,
    val accessTokenExpirationTime: Long,
)
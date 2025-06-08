package io.github.alstn113.goodpon.infra.email

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties
data class AwsSesClientProperties(
    val accessKey: String,
    val secretKey: String,
    val senderEmail: String,
)

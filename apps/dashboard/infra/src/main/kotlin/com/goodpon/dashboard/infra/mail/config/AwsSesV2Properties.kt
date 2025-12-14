package com.goodpon.dashboard.infra.mail.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile

@Profile("prod")
@ConfigurationProperties(prefix = "aws.ses")
data class AwsSesV2Properties(
    val region: String,
    val accessKeyId: String,
    val secretAccessKey: String,
)
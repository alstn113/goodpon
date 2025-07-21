package com.goodpon.infra.aws.ses

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile

@ConfigurationProperties(prefix = "aws.ses")
@Profile("prod")
data class AwsSesV2Properties(
    val region: String,
    val accessKeyId: String,
    val secretAccessKey: String,
)

package com.goodpon.infra.aws.ses

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.ses")
data class AwsSesV2Properties(
    val region: String,
    val accessKeyId: String,
    val secretAccessKey: String,
)

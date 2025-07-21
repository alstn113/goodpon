package com.goodpon.infra.aws.ses

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sesv2.SesV2Client

@Configuration
@Profile("prod")
@EnableConfigurationProperties(AwsSesV2Properties::class)
class AwsSesV2Configuration(
    private val properties: AwsSesV2Properties,
) {

    @Bean
    fun awsSesClient(): SesV2Client {
        val credentials = AwsBasicCredentials.builder()
            .accessKeyId(properties.accessKeyId)
            .secretAccessKey(properties.secretAccessKey)
            .build()

        return SesV2Client.builder()
            .region(Region.of(properties.region))
            .credentialsProvider { credentials }
            .build()
    }
}
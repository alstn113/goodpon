package com.goodpon.api.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages =
        [
            "com.goodpon.api.core",
            "com.goodpon.common",
        ]
)
@ConfigurationPropertiesScan(
    basePackages =
        [
            "com.goodpon.api.core",
            "com.goodpon.common",
        ]
)
class CoreApiApplication

fun main(args: Array<String>) {
    runApplication<CoreApiApplication>(*args)
}

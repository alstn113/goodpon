package com.goodpon.api.dashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages =
        [
            "com.goodpon.api.dashboard",
            "com.goodpon.common",
        ]
)
@ConfigurationPropertiesScan(
    basePackages =
        [
            "com.goodpon.api.dashboard",
            "com.goodpon.common",
        ]
)
class DashboardApiApplication

fun main(args: Array<String>) {
    runApplication<DashboardApiApplication>(*args)
}

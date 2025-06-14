package com.goodpon.api.dashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages =
        [
            "com.goodpon.api.dashboard",
            "com.goodpon.core",
            "com.goodpon.infra.db",
            "com.goodpon.infra.common",
            "com.goodpon.infra.security",
        ]
)
class DashboardApiApplication

fun main(args: Array<String>) {
    runApplication<DashboardApiApplication>(*args)
}

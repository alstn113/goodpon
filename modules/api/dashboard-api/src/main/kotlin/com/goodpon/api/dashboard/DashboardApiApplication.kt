package com.goodpon.api.dashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.goodpon.api.dashboard",
        "com.goodpon.application.dashboard",
        "com.goodpon.infra"
    ]
)
class DashboardApiApplication

fun main(args: Array<String>) {
    runApplication<DashboardApiApplication>(*args)
}

package com.goodpon.dashboard.infra

import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.goodpon.dashboard.infra",
        "com.goodpon.infra"
    ]
)
class DashboardInfraTestApplication
package com.goodpon.partner.infra

import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.goodpon.partner.infra",
        "com.goodpon.infra"
    ]
)
class PartnerInfraTestApplication

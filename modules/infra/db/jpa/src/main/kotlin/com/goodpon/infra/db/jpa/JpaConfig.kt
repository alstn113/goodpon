package com.goodpon.infra.db.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan("com.goodpon.infra.db.jpa")
@EnableJpaRepositories("com.goodpon.infra.db.jpa")
@EnableJpaAuditing
class JpaConfig
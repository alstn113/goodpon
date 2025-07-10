package com.goodpon.dashboard.api.support

import com.goodpon.infra.db.jpa.MySQLContainerInitializer
import com.goodpon.infra.redis.RedisContainerInitializer
import com.goodpon.infra.redis.RedisDataCleanupExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [
        MySQLContainerInitializer::class,
        RedisContainerInitializer::class
    ]
)
@ExtendWith(
    com.goodpon.infra.db.jpa.MySQLDataCleanupExtension::class,
    RedisDataCleanupExtension::class
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest
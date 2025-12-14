package com.goodpon.couponissuer.bootstrap.support

import com.goodpon.infra.jpa.MySQLContainerInitializer
import com.goodpon.infra.jpa.MySQLDataCleanupExtension
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
        RedisContainerInitializer::class,
    ]
)
@ExtendWith(
    MySQLDataCleanupExtension::class,
    RedisDataCleanupExtension::class,
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest
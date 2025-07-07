package com.goodpon.infra.redis

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor


@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = [RedisContainerInitializer::class])
@ExtendWith(RedisDataCleanupExtension::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractRedisIntegrationTest
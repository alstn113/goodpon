package com.goodpon.infra.redis.support

import com.goodpon.infra.redis.RedisContainerInitializer
import com.goodpon.infra.redis.RedisDataCleanupExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor

@SpringBootTest
@ContextConfiguration(initializers = [RedisContainerInitializer::class])
@ExtendWith(RedisDataCleanupExtension::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest
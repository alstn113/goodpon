package com.goodpon.support

import com.goodpon.goodpon.support.cleaner.DataCleanupExtension
import com.goodpon.goodpon.support.testcontainers.MySQLContainerInitializer
import com.goodpon.support.testcontainers.RedisContainerInitializer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor


@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [
        com.goodpon.support.testcontainers.RedisContainerInitializer::class,
        MySQLContainerInitializer::class
    ]
)
@ExtendWith(DataCleanupExtension::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest

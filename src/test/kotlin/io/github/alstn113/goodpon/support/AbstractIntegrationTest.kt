package io.github.alstn113.goodpon.support

import io.github.alstn113.goodpon.support.cleaner.DataCleanupExtension
import io.github.alstn113.goodpon.support.testcontainers.MySQLContainerInitializer
import io.github.alstn113.goodpon.support.testcontainers.RedisContainerInitializer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor


@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [
        RedisContainerInitializer::class,
        MySQLContainerInitializer::class
    ]
)
@ExtendWith(DataCleanupExtension::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest

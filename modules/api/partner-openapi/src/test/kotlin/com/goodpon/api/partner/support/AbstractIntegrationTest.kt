package com.goodpon.api.partner.support

import com.goodpon.infra.db.jpa.MySQLContainerInitializer
import com.goodpon.infra.db.jpa.MySQLDataCleanupExtension
import com.goodpon.infra.kafka.KafkaContainerInitializer
import com.goodpon.infra.kafka.KafkaTopicCleanupExtension
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
        KafkaContainerInitializer::class
    ]
)
@ExtendWith(
    MySQLDataCleanupExtension::class,
    RedisDataCleanupExtension::class,
    KafkaTopicCleanupExtension::class
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest
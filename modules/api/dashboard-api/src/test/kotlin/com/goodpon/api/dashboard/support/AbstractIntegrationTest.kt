package com.goodpon.api.dashboard.support

import com.goodpon.application.dashboard.auth.service.listener.VerificationEmailEventListener
import com.goodpon.infra.db.jpa.MySQLContainerInitializer
import com.goodpon.infra.db.jpa.MySQLDataCleanupExtension
import com.goodpon.infra.redis.RedisContainerInitializer
import com.goodpon.infra.redis.RedisDataCleanupExtension
import com.ninjasquad.springmockk.MockkBean
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
    MySQLDataCleanupExtension::class,
    RedisDataCleanupExtension::class
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest {

    @MockkBean
    protected lateinit var verificationEmailEventListener: VerificationEmailEventListener
}
package com.goodpon.partner.openapi.support

import com.goodpon.infra.db.jpa.MySQLContainerInitializer
import com.goodpon.infra.db.jpa.MySQLDataCleanupExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = [MySQLContainerInitializer::class])
@ExtendWith(MySQLDataCleanupExtension::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractIntegrationTest
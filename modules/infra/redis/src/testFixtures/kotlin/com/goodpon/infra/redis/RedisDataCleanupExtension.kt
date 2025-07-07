package com.goodpon.infra.redis

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class RedisDataCleanupExtension : AfterEachCallback {

    override fun afterEach(context: ExtensionContext) {
        val cleaner = SpringExtension.getApplicationContext(context)
            .getBean(RedisDataCleaner::class.java)

        cleaner.clear()
    }
}
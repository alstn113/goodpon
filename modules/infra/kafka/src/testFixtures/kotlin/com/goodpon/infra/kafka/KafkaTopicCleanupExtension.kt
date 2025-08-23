package com.goodpon.infra.kafka

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class KafkaTopicCleanupExtension : AfterEachCallback {

    override fun afterEach(context: ExtensionContext) {
        val cleaner = SpringExtension.getApplicationContext(context)
            .getBean(KafkaTopicCleaner::class.java)

        cleaner.clear()
    }
}
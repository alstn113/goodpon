package com.goodpon.infra.db.jpa

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class MySQLDataCleanupExtension : AfterEachCallback {

    override fun afterEach(context: ExtensionContext) {
        val cleaner = SpringExtension.getApplicationContext(context)
            .getBean(com.goodpon.infra.db.jpa.MySQLDataCleaner::class.java)

        cleaner.clear()
    }
}
package com.goodpon.partner.openapi.config

import com.goodpon.domain.BaseException
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun handleUncaughtException(e: Throwable, method: Method, vararg params: Any?) {
        if (e is BaseException) {
            log.warn("CoreException : {}", e.message, e)
        } else {
            log.error("Exception : {}", e.message, e)
        }
    }
}
package io.github.alstn113.goodpon.api.goodpon.config

import io.github.alstn113.goodpon.support.error.CoreException
import io.github.alstn113.goodpon.support.error.ErrorLevel
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun handleUncaughtException(e: Throwable, method: Method, vararg params: Any?) {
        if (e is CoreException) {
            when (e.errorType.errorLevel) {
                ErrorLevel.ERROR -> log.error("CoreException : {}", e.message, e)
                ErrorLevel.WARN -> log.warn("CoreException : {}", e.message, e)
                else -> log.info("CoreException : {}", e.message, e)
            }
        } else {
            log.error("Exception : {}", e.message, e)
        }
    }
}
package  com.goodpon.api.partner.security.filter

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class CachedBodyHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val cachedBody: ByteArray = try {
        request.inputStream.readBytes()
    } catch (e: Exception) {
        throw IllegalStateException("Failed to read the request body", e)
    }

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(cachedBody)

        return object : ServletInputStream() {
            override fun read(): Int = byteArrayInputStream.read()

            override fun isFinished(): Boolean = byteArrayInputStream.available() == 0

            override fun isReady(): Boolean = true

            override fun setReadListener(readListener: ReadListener) {}
        }
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(getInputStream(), StandardCharsets.UTF_8))
    }

    fun getCachedBody(): ByteArray = cachedBody
}

package com.goodpon.partner.bootstrap.support

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.infra.jpa.MySQLContainerInitializer
import com.goodpon.infra.jpa.MySQLDataCleanupExtension
import com.goodpon.infra.redis.RedisContainerInitializer
import com.goodpon.infra.redis.RedisDataCleanupExtension
import com.goodpon.partner.api.response.ApiResponse
import com.goodpon.partner.api.response.ErrorMessage
import com.goodpon.partner.api.security.ApiKeyHeader
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(
    initializers = [
        MySQLContainerInitializer::class,
        RedisContainerInitializer::class,
    ]
)
@ExtendWith(
    MySQLDataCleanupExtension::class,
    RedisDataCleanupExtension::class,
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractEndToEndTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.requestSpecification = RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setPort(port)
            .log(LogDetail.ALL)
            .build()
        RestAssured.responseSpecification = ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build()
    }

    fun RequestSpecification.withApiKeyHeaders(clientId: String, clientSecret: String): RequestSpecification {
        return this
            .header(ApiKeyHeader.CLIENT_ID.headerName, clientId)
            .header(ApiKeyHeader.CLIENT_SECRET.headerName, clientSecret)
    }

    fun RequestSpecification.withIdempotencyKey(idempotencyKey: String): RequestSpecification {
        return this.header("Idempotency-Key", idempotencyKey)
    }

    final inline fun <reified T> Response.toApiResponse(): T {
        val response: ApiResponse<T> = objectMapper.readValue(
            this.asString(),
            object : TypeReference<ApiResponse<T>>() {}
        )

        return response.data ?: throw IllegalStateException("응답 데이터가 없습니다.")
    }

    final inline fun <reified T> Response.toApiErrorResponse(): ErrorMessage {
        val response = objectMapper.readValue(
            this.asString(),
            object : TypeReference<ApiResponse<T>>() {}
        )

        return response.error ?: throw IllegalStateException("오류 메시지가 없습니다.")
    }

    final inline fun <reified R> ErrorMessage.extractErrorData(): R {
        val data = objectMapper.convertValue(
            this.data,
            object : TypeReference<R>() {}
        )

        return data
    }
}
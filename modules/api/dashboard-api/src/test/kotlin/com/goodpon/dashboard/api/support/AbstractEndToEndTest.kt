package com.goodpon.dashboard.api.support

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.application.auth.service.VerificationTokenGenerator
import com.goodpon.infra.db.jpa.MySQLContainerInitializer
import com.goodpon.infra.db.jpa.MySQLDataCleanupExtension
import com.goodpon.infra.redis.RedisContainerInitializer
import com.goodpon.infra.redis.RedisDataCleanupExtension
import com.ninjasquad.springmockk.MockkBean
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
        RedisContainerInitializer::class
    ]
)
@ExtendWith(
    MySQLDataCleanupExtension::class,
    RedisDataCleanupExtension::class
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractEndToEndTest {

    @MockkBean
    protected lateinit var verificationTokenGenerator: VerificationTokenGenerator

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

    fun RequestSpecification.withAuthHeader(token: String): RequestSpecification {
        return this.header("Authorization", "Bearer $token")
    }

    final inline fun <reified T> Response.toApiResponse(): T {
        val apiResponse = objectMapper.readValue(this.asString(), object : TypeReference<ApiResponse<Any>>() {})
        return objectMapper.convertValue(apiResponse.data, T::class.java)
    }
}
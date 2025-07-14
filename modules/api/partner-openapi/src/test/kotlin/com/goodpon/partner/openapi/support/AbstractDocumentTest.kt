package com.goodpon.partner.openapi.support

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation.headerWithName
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.goodpon.partner.openapi.security.ApiKeyHeader
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(RestDocumentationExtension::class)
abstract class AbstractDocumentTest : AbstractWebTest() {

    @BeforeEach
    fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider,
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }

    fun MockHttpServletRequestBuilder.withApiKeyHeaders(): MockHttpServletRequestBuilder {
        return this
            .header(ApiKeyHeader.CLIENT_ID.headerName, "{client-id}")
            .header(ApiKeyHeader.CLIENT_SECRET.headerName, "{client-secret}")
    }

    fun ResultActions.andDocument(
        identifier: String,
        resourceSnippetParameters: ResourceSnippetParameters,
    ): ResultActions {
        return this.andDo(
            MockMvcRestDocumentationWrapper.document(
                identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(resourceSnippetParameters)
            )
        )
    }

    fun apiKeyHeaderFields() = listOf(
        headerWithName(ApiKeyHeader.CLIENT_ID.headerName).description("API 클라이언트 ID"),
        headerWithName(ApiKeyHeader.CLIENT_SECRET.headerName).description("API 클라이언트 Secret")
    )

    fun failureResponseFields() = listOf(
        fieldWithPath("traceId").type(JsonFieldType.STRING).description("오류 추적 ID"),
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.NULL).description("결과 데이터 (실패 시 null)"),
        fieldWithPath("error").type(JsonFieldType.OBJECT).description("오류 정보"),
        fieldWithPath("error.code").type(JsonFieldType.STRING).description("오류 코드"),
        fieldWithPath("error.message").type(JsonFieldType.STRING).description("오류 메시지"),
        fieldWithPath("error.data").type(JsonFieldType.NULL).description("오류 데이터 (없을 경우 null)")
    )
}
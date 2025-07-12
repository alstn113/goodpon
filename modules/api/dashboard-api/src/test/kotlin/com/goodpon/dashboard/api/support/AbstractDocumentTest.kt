package com.goodpon.dashboard.api.support

import com.epages.restdocs.apispec.HeaderDescriptorWithType
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation.headerWithName
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import org.apache.http.HttpHeaders
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

    fun MockHttpServletRequestBuilder.withAuthHeader(): MockHttpServletRequestBuilder {
        return this.header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
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

    fun authHeaderFields(): HeaderDescriptorWithType {
        return headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰 (Bearer {access-token})")
    }

    fun failureResponseFields() = listOf(
        fieldWithPath("result").type(JsonFieldType.STRING).description("요청 결과 (SUCCESS/ERROR)"),
        fieldWithPath("data").type(JsonFieldType.NULL).description("결과 데이터 (실패 시 null)"),
        fieldWithPath("error").type(JsonFieldType.OBJECT).description("오류 정보"),
        fieldWithPath("error.code").type(JsonFieldType.STRING).description("오류 코드"),
        fieldWithPath("error.message").type(JsonFieldType.STRING).description("오류 메시지"),
        fieldWithPath("error.data").type(JsonFieldType.NULL).description("오류 데이터 (없을 경우 null)")
    )
}
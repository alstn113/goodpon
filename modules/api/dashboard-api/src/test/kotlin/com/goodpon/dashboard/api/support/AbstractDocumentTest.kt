package com.goodpon.dashboard.api.support

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
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

    fun MockHttpServletRequestBuilder.withAuthHeader(token: String): MockHttpServletRequestBuilder {
        return this.header("Authorization", "Bearer $token")
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
}
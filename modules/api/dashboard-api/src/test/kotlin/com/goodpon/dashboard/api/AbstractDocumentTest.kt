package com.goodpon.dashboard.api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


@ExtendWith(RestDocumentationExtension::class)
abstract class AbstractDocumentTest : AbstractControllerTest() {

    @BeforeEach
    fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider,
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                    .uris()
                    .withScheme("https")
                    .withHost("dashboard.goodpon.site")
                    .withPort(443)
                    .and()
                    .operationPreprocessors()
                    .withRequestDefaults(modifyUris().host("dashboard.goodpon.site").removePort(), prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .build()
    }
}
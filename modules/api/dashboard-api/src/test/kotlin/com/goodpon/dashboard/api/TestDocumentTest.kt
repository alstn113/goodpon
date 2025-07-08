package com.goodpon.dashboard.api

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TestDocumentTest : AbstractDocumentTest() {

    @Test
    fun `test`() {
        mockMvc.perform(get("/v1/test").accept("application/json"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("message", `is`("안녕 😘")))
            .andDo(
                MockMvcRestDocumentationWrapper.document(
                    identifier = "ㅎㅇ",
                    resourceDetails = ResourceSnippetParametersBuilder()
                        .tag("테스트")
                        .description("테스트 API 문서")
                        .responseFields(
                            fieldWithPath("message").description("응답 메시지")
                        )
                )
            )
    }
}